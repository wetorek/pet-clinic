package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RestVisitController.class)
public class RestVisitControllerIT {
    private static final String PATH = "/api/v1/visits";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private VisitService visitService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllVisits() throws Exception {
        var visits = List.of(createVisitResponseDto());

        when(visitService.getAllVisits()).thenReturn(visits);

        mvc.perform(get(PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].animalId", is(1)))
                .andExpect(jsonPath("$[0].vetId", is(1)))
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[0].surgeryId", is(1)))
                .andExpect(jsonPath("$[0].description", is("")));
    }

    @Test
    void getVisitByIdTest() throws Exception {
        var visitResponse = createVisitResponseDto();
        when(visitService.getVisitById(1)).thenReturn(Optional.of(visitResponse));

        mvc.perform(get(PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.animalId", is(1)))
                .andExpect(jsonPath("$.vetId", is(1)))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.surgeryId", is(1)))
                .andExpect(jsonPath("$.description", is("")))
                .andExpect(jsonPath("$.animalId", is(1)))
                .andExpect(jsonPath("$.vetId", is(1)))
                .andExpect(jsonPath("$.price", is(10)))
                .andExpect(jsonPath("$.status", is("PLANNED")));
    }

    private VisitResponseDto createVisitResponseDto() {
        return new VisitResponseDto(1, LocalDateTime.of(1999, 7, 12, 10, 0), Duration.ofMinutes(20), "PLANNED", BigDecimal.TEN,
                "", 1, 1, 1, 1);
    }
}
