package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetDescriptionRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetStatusRequestDto;
import com.clinic.pet.petclinic.entity.*;
import com.clinic.pet.petclinic.exceptions.IllegalVisitStateException;
import com.clinic.pet.petclinic.exceptions.VisitNotFoundException;
import com.clinic.pet.petclinic.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {
    private static final LocalDate LOCAL_DATE_1 = LocalDate.of(1999, 7, 12);
    private static final LocalDateTime LOCAL_DATE_TIME_1 = LocalDateTime.of(1999, 7, 12, 10, 0);

    @InjectMocks
    private VisitService visitService;
    @Mock
    private VisitRepository visitRepository;
    @Mock
    private AnimalRepository animalRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private SurgeryRepository surgeryRepository;
    @Mock
    private VetRepository vetRepository;
    @Mock
    private VisitMapper visitMapper;
    @Spy
    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2018-08-19T16:00:00.00Z"), ZoneId.systemDefault());
    }

    @Test
    void getAllVisits() {
        var visit = createVisit();
        var responseDto = createVisitResponseDto();
        when(visitRepository.findAll()).thenReturn(List.of(visit));
        when(visitMapper.mapListToDto(any())).thenReturn(List.of(responseDto));

        var actual = visitService.getAllVisits();

        assertThat(actual).containsExactly(responseDto);
        verify(visitRepository, times(1)).findAll();
        verify(visitMapper, times(1)).mapListToDto(List.of(visit));
    }

    @Test
    void getExistingVisitById() {
        var visit = createVisit();
        var responseDto = createVisitResponseDto();
        when(visitRepository.findById(any())).thenReturn(Optional.of(visit));
        when(visitMapper.mapToDto(any())).thenReturn(responseDto);

        var actual = visitService.getVisitById(1);

        assertThat(actual).contains(responseDto);
        verify(visitRepository, times(1)).findById(1);
        verify(visitMapper, times(1)).mapToDto(visit);
    }

    @Test
    void getNotExistingVisitById() {
        when(visitRepository.findById(any())).thenReturn(Optional.empty());

        var actual = visitService.getVisitById(12312);

        assertThat(actual).isEmpty();
        verify(visitRepository, only()).findById(12312);
    }

//    @Test
//    void createAStandardVisit(){
//
//        LocalDateTime dateTime = LocalDateTime.now(clock);
//        dateTime.plus(Duration.ofDays(7));
//        VisitRequestDto requestDto = new VisitRequestDto(dateTime, Duration.ofMinutes(15), 1,"PLANNED",
//                BigDecimal.valueOf(100), 1, "", 1 );
//        Customer customer = new Customer(1, "John", "Doe");
//        Animal animal =  new Animal(1, "animalName", LOCAL_DATE_1, AnimalSpecies.HAMSTER, customer);
//        Vet vet = new Vet(1, "Wiliam", "Williams", LocalTime.of(10, 0), LocalTime.of(18, 0), null);
//        Surgery surgery = new Surgery(1, "surgery1");
//        Visit expected = new Visit(1, dateTime, Duration.ofMinutes(15),
//                Status.PLANNED, BigDecimal.valueOf(100), "", animal, customer, vet, surgery);
//        VisitResponseDto responseDto = new VisitResponseDto(1, dateTime, Duration.ofMinutes(15), "PLANNED", BigDecimal.valueOf(100), "", 1,1,1,1);
//        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
//        when(animalRepository.findById(1)).thenReturn(Optional.of(animal));
//        when(vetRepository.findById(1)).thenReturn(Optional.of(vet));
//        when(visitMapper.mapToEntity(requestDto, animal, vet,customer, surgery)).thenReturn(expected);
//        when(visitRepository.save(expected)).thenReturn(expected);
//        when(visitMapper.mapToDto(expected)).thenReturn(responseDto);
//
//        visitService.createVisit(requestDto);
//    }


    @Test
    void createAOverlappingVisit() {
        LocalDateTime dateTime = LocalDateTime.now(clock);
        dateTime.plus(Duration.ofDays(7));

        Customer customer = new Customer(1, "John", "Doe");
        Animal animal = new Animal(1, "animalName", LOCAL_DATE_1, AnimalSpecies.HAMSTER, customer);
        Vet vet = new Vet(1, "Wiliam", "Williams", LocalTime.of(10, 0), LocalTime.of(18, 0));
        Surgery surgery = new Surgery(1, "surgery1");

        Visit expected = new Visit(1, dateTime, Duration.ofMinutes(15),
                Status.PLANNED, BigDecimal.valueOf(100), "", animal, customer, vet, surgery);

        visitRepository.save(expected);
        VisitRequestDto requestDto = new VisitRequestDto(dateTime, Duration.ofMinutes(15), 1, "PLANNED",
                BigDecimal.valueOf(100), 1, "", 1);

        when(visitRepository.existOverlapping(requestDto.getStartTime(), requestDto.getStartTime().plus(requestDto.getDuration()))).thenReturn(List.of(expected));

        assertThrows(IllegalVisitStateException.class, () -> visitService.createVisit(requestDto));
    }

    @Test
    void deleteExistingVisit() {
        when(visitRepository.existsById(any())).thenReturn(true);

        visitService.delete(1);

        verify(visitRepository, times(1)).existsById(1);
        verify(visitRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteNotExistingVisit() {
        when(visitRepository.existsById(any())).thenReturn(false);

        var thrown = catchThrowable(() -> visitService.delete(1));

        assertThat(thrown)
                .isInstanceOf(VisitNotFoundException.class);
        verify(visitRepository, only()).existsById(1);
    }

    @Test
    void changeDescriptionValidState() {
        var description = new VisitSetDescriptionRequestDto("descr");
        var visit = createVisit();
        visit.setStatus(Status.FINISHED);
        var responseDto = createVisitResponseDto();
        when(visitRepository.findById(any())).thenReturn(Optional.of(visit));
        when(visitRepository.save(any())).thenReturn(visit);
        when(visitMapper.mapToDto(any())).thenReturn(responseDto);

        visitService.changeDescription(1, description);

        assertThat(visit.getDescription()).isEqualTo(description.getDescription());
        verify(visitRepository, times(1)).findById(1);
        verify(visitRepository, times(1)).save(visit);
        verify(visitMapper, times(1)).mapToDto(visit);
    }

    @Test
    void changeDescriptionInvalidVisitState() {
        var description = new VisitSetDescriptionRequestDto("descr");
        var visit = createVisit();
        visit.setStatus(Status.PLANNED);
        when(visitRepository.findById(any())).thenReturn(Optional.of(visit));

        var thrown = catchThrowable(() -> visitService.changeDescription(1, description));

        assertThat(thrown).isInstanceOf(IllegalVisitStateException.class);
        verify(visitRepository, times(1)).findById(1);
    }

    @Test
    void changeStusValid() {
        var visitSetStatusRequestDto = new VisitSetStatusRequestDto("FINISHED");
        var visit = createVisit();
        visit.setStatus(Status.PLANNED);
        var responseDto = createVisitResponseDto();
        when(visitRepository.findById(any())).thenReturn(Optional.of(visit));
        when(visitRepository.save(any())).thenReturn(visit);
        when(visitMapper.mapToDto(any())).thenReturn(responseDto);
        when(visitMapper.mapStringToStatus(any())).thenReturn(Status.FINISHED);

        visitService.changeVisitStatus(1, visitSetStatusRequestDto);

        assertThat(visit.getStatus()).isEqualTo(Status.FINISHED);
        verify(visitRepository, times(1)).findById(1);
        verify(visitRepository, times(1)).save(visit);
        verify(visitMapper, times(1)).mapToDto(visit);
    }

    @Test
    void changeStusInValid() {
        var visitSetStatusRequestDto = new VisitSetStatusRequestDto("PLANNED");
        var visit = createVisit();
        visit.setStatus(Status.PLANNED);
        when(visitRepository.findById(any())).thenReturn(Optional.of(visit));
        when(visitMapper.mapStringToStatus(any())).thenReturn(Status.PLANNED);

        var thrown = catchThrowable(() -> visitService.changeVisitStatus(1, visitSetStatusRequestDto));

        assertThat(thrown).isInstanceOf(IllegalVisitStateException.class);
        verify(visitRepository, times(1)).findById(1);
    }

    private Visit createVisit() {
        var owner = new Customer(1, "John", "Doe");
        var animal = new Animal(1, "animal1", LOCAL_DATE_1, AnimalSpecies.CAT, owner);
        var vet = new Vet(1, "Walt", "Kowalski", LocalTime.of(8, 0), LocalTime.of(16, 0));
        var surgery = new Surgery(1, "Surgery 1");
        return new Visit(1, LOCAL_DATE_TIME_1, Duration.ofMinutes(20), Status.PLANNED, BigDecimal.TEN, "", animal, owner, vet, surgery);
    }

    private VisitResponseDto createVisitResponseDto() {
        return new VisitResponseDto(1, LOCAL_DATE_TIME_1, Duration.ofMinutes(20), "PLANNED", BigDecimal.TEN,
                "", 1, 1, 1, 1);
    }

}