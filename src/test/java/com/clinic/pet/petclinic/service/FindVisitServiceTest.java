package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.entity.Vet;
import com.clinic.pet.petclinic.repository.VetRepository;
import com.clinic.pet.petclinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FindVisitServiceTest {

    @InjectMocks
    private FindVisitService findVisitService;
    @Mock
    private VetRepository vetRepository;
    @Mock
    private VisitRepository visitRepository;
    @Mock
    private VetMapper vetMapper;

    private Clock clock;

    @BeforeEach
    void setup(){
        clock = Clock.fixed(Instant.parse("2018-08-19T16:00:00.00Z"), ZoneId.systemDefault());
    }

    @Test
    void theSameDateTime(){
        LocalDateTime time1 = LocalDateTime.of(2022,4,12,15,0,0);
        LocalDateTime time2 = LocalDateTime.of(2022,4,12,15,0,0);

        Vet vet = new Vet(1, "John", "Doe", LocalTime.of(10,0), LocalTime.of(18,0),null);
        List<Vet> vets = List.of(vet);

        when(vetRepository.findAll()).thenReturn(vets);

        assertTrue( findVisitService.findFreeSlots(time1,time2).isEmpty());
    }

    @Test
    void oneSlot(){
        LocalDateTime time1 = LocalDateTime.of(2022,4,12,15,0,0);
        LocalDateTime time2 = LocalDateTime.of(2022,4,12,15,15,0);

        Vet vet = new Vet(1, "John", "Doe", LocalTime.of(10,0), LocalTime.of(18,0),null);
        List<Vet> vets = List.of(vet);

        when(vetRepository.findAll()).thenReturn(vets);
        assertEquals(findVisitService.findFreeSlots(time1, time2).size(), 1);
    }
    @Test
    void twoVets(){
        LocalDateTime time1 = LocalDateTime.of(2022,4,12,14,0,0);
        LocalDateTime time2 = LocalDateTime.of(2022,4,12,15,15,0);

        Vet vet1 = new Vet(1, "John", "Doe", LocalTime.of(10,0), LocalTime.of(18,0),null);
        Vet vet2 = new Vet(2, "Alex", "Smith", LocalTime.of(9,0), LocalTime.of(15,0),null);
        List<Vet> vets = List.of(vet1, vet2);

        when(vetRepository.findAll()).thenReturn(vets);

        assertEquals(findVisitService.findFreeSlots(time1, time2).size(), 8);
        assertEquals(time1, findVisitService.findFreeSlots(time1, time2).get(0).getStart());
        assertEquals(time1.plusMinutes(15), findVisitService.findFreeSlots(time1, time2).get(1).getStart());
        assertEquals(time1.plusMinutes(30), findVisitService.findFreeSlots(time1, time2).get(2).getStart());
        assertEquals(time1.plusMinutes(45), findVisitService.findFreeSlots(time1, time2).get(3).getStart());
    }
    @Test
    void twoVetsOneVetIsAvailable(){
        LocalDateTime time1 = LocalDateTime.of(2022,4,12,14,0,0);
        LocalDateTime time2 = LocalDateTime.of(2022,4,12,15,15,0);

        Vet vet1 = new Vet(1, "John", "Doe", LocalTime.of(10,0), LocalTime.of(18,0),null);
        Vet vet2 = new Vet(2, "Alex", "Smith", LocalTime.of(9,0), LocalTime.of(10,0),null);
        Vet vet3 = new Vet(3, "Wiliam", "Smith", LocalTime.of(9,0), LocalTime.of(14,0),null);
        List<Vet> vets = List.of(vet1, vet2, vet3);

        when(vetRepository.findAll()).thenReturn(vets);
        assertEquals(findVisitService.findFreeSlots(time1,time2).size(), 5);
    }
    @Test
    void twoDateAfterVetAvailability(){
        LocalDateTime time1 = LocalDateTime.of(2022,4,12,20,0,0);
        LocalDateTime time2 = LocalDateTime.of(2022,4,12,20,15,0);

        Vet vet1 = new Vet(1, "John", "Doe", LocalTime.of(10,0), LocalTime.of(18,0),null);
        Vet vet2 = new Vet(2, "Alex", "Smith", LocalTime.of(9,0), LocalTime.of(10,0),null);
        Vet vet3 = new Vet(3, "Wiliam", "Smith", LocalTime.of(9,0), LocalTime.of(14,0),null);

        List<Vet> vets = List.of(vet1, vet2, vet3);

        when(vetRepository.findAll()).thenReturn(vets);
        assertEquals(findVisitService.findFreeSlots(time1,time2).size(), 0);
    }
}
