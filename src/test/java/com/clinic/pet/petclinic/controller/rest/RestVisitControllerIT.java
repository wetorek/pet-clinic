package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetStatusRequestDto;
import com.clinic.pet.petclinic.entity.*;
import com.clinic.pet.petclinic.service.VisitMapper;
import com.clinic.pet.petclinic.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RestVisitControllerIT {

    private final String PATH = "/api/v1/visits";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private VisitService visitService;
    @Autowired
    private VisitMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;
    private Clock clock;

    @BeforeEach
    void setup(){
        clock = Clock.fixed(Instant.parse("2018-08-19T16:00:00.00Z"), ZoneId.systemDefault());
    }

    @Test
    void getAllVisits() throws Exception {
        Customer customer = new Customer(1, "John", "Doe");
        Animal animal = new Animal(1,"a1", LocalDate.of(2012, 12,4),
                AnimalSpecies.HAMSTER, customer);
        Vet vet = new Vet(1, "Alex", "Smith", LocalTime.of(10,0), LocalTime.of(16,0), null);
        Surgery surgery = new Surgery(1, "surgery1");
        Visit visit1 = new Visit(1, LocalDateTime.now(clock), Duration.ofMinutes(15), Status.PLANNED, BigDecimal.valueOf(75),
                "", animal,customer, vet, surgery);
        Visit visit2 = new Visit(2,  LocalDateTime.now(clock).plusMinutes(30), Duration.ofMinutes(15), Status.PLANNED, BigDecimal.valueOf(75),
                "", animal, customer, vet, surgery);

        List <VisitResponseDto> visits = List.of(mapper.mapToDto(visit1),mapper.mapToDto(visit2));

        when(visitService.getAllVisits()).thenReturn(visits);

        mvc.perform(get(PATH)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].animalId",is(1)))
                .andExpect(jsonPath("$[0].vetId",is(1)))
                .andExpect(jsonPath("$[0].customerId",is(1)))
                .andExpect(jsonPath("$[0].surgeryId",is(1)))
                .andExpect(jsonPath("$[0].description",is("")))
                .andExpect(jsonPath("$[1].animalId", is(1)));
    }

    @Test
    void getVisitByIdTest() throws Exception {
        Customer customer = new Customer(1, "John", "Doe");
        Animal animal = new Animal(1,"a1", LocalDate.of(2012, 12,4),
                AnimalSpecies.HAMSTER, customer);
        Vet vet = new Vet(1, "Alex", "Smith", LocalTime.of(10,0), LocalTime.of(16,0), null);
        Surgery surgery = new Surgery(1, "surgery1");
        Visit visit1 = new Visit(1, LocalDateTime.now(clock), Duration.ofMinutes(15), Status.PLANNED, BigDecimal.valueOf(75),
                "", animal,customer, vet, surgery);

        when(visitService.getVisitById(1)).thenReturn(Optional.of(mapper.mapToDto(visit1)));

        mvc.perform(get(PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.animalId",is(1)))
                .andExpect(jsonPath("$.vetId",is(1)))
                .andExpect(jsonPath("$.customerId",is(1)))
                .andExpect(jsonPath("$.surgeryId",is(1)))
                .andExpect(jsonPath("$.description",is("")))
                .andExpect(jsonPath("$.animalId", is(1)))
                .andExpect(jsonPath("$.vetId", is(1)))
                .andExpect(jsonPath("$.price", is(75)))
                .andExpect(jsonPath("$.status", is("PLANNED")));
    }
//    @Test
//    void updateVisitStatusTest() throws Exception {
//        VisitSetStatusRequestDto setStatusRequestDto = new VisitSetStatusRequestDto("NOT_APPEARED");
//
//        VisitResponseDto expected = new VisitResponseDto(1, LocalDateTime.now(clock).minusMinutes(70), Duration.ofMinutes(15),
//                "NOT_APPEARED", BigDecimal.valueOf(75), "", 1, 1, 1, 1);
//
//        when(visitService.changeVisitStatus(1, setStatusRequestDto)).thenReturn(expected);
//
//        mvc.perform(patch(PATH + "/1/status")
//                    .contentType("application/json")
//                    .accept("application/json+hal")
//                    .content(objectMapper.writeValueAsString(setStatusRequestDto)))
//                .andExpect(jsonPath("$.id", is(1)))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(jsonPath("$.price", is(75)))
//                .andExpect(jsonPath("$.duration", is(Duration.ofMinutes(15))))
//                .andExpect(jsonPath("$.vetId", is(1)));
//    }

}
