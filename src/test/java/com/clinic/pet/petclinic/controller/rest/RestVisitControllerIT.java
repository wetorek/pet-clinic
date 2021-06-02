package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.auth.AuthenticatedUser;
import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Role;
import com.clinic.pet.petclinic.exceptions.VisitNotFoundException;
import com.clinic.pet.petclinic.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RestVisitControllerIT {
    private static final String PATH = "/api/v1/visits";
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private VisitService visitService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void initialize() {
        mvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllVisits() throws Exception {
        var visits = List.of(createVisitResponseDto());

        when(visitService.getAllVisits()).thenReturn(visits);

        mvc.perform(get(PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.visitResponseDtoList[0].animalId", is(1)))
                .andExpect(jsonPath("_embedded.visitResponseDtoList[0].vetId", is(1)))
                .andExpect(jsonPath("_embedded.visitResponseDtoList[0].customerId", is(1)))
                .andExpect(jsonPath("_embedded.visitResponseDtoList[0].surgeryId", is(1)))
                .andExpect(jsonPath("_embedded.visitResponseDtoList[0].description", is("")));
        verify(visitService, only()).getAllVisits();
    }

    @Test
    void getVisitByIdTest() throws Exception {
        var visitResponse = createVisitResponseDto();
        when(visitService.getVisitById(1)).thenReturn(Optional.of(visitResponse));
        var vetUser = (UserDetails) new AuthenticatedUser(1, "client", "dsad", AccountState.ACTIVE, Collections.singletonList(Role.ROLE_VET));

        mvc.perform(get(PATH + "/1")
                .with(user(vetUser))
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
        verify(visitService, only()).getVisitById(1);
    }

    @Test
    void createVisitTest() throws Exception {
        var visitResponse = createVisitResponseDto();
        when(visitService.createVisit(any())).thenReturn(visitResponse);
        var visitRequest = creteVisitRequestDto();
        var clientUser = (UserDetails) new AuthenticatedUser(1, "client", "dsad", AccountState.ACTIVE, Collections.singletonList(Role.ROLE_CLIENT));

        mvc.perform(
                post(PATH)
                        .contentType("application/json")
                        .accept("application/hal+json")
                        .with(user(clientUser))
                        .content(objectMapper.writeValueAsString(visitRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.animalId", is(1)))
                .andExpect(jsonPath("$.vetId", is(1)))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.surgeryId", is(1)))
                .andExpect(jsonPath("$.description", is("")))
                .andExpect(jsonPath("$.animalId", is(1)))
                .andExpect(jsonPath("$.vetId", is(1)))
                .andExpect(jsonPath("$.price", is(10)))
                .andExpect(jsonPath("$.status", is("PLANNED")));
        verify(visitService, only()).createVisit(visitRequest);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteExistingVisitByIdTest() throws Exception {
        mvc.perform(delete(PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));

        verify(visitService, only()).delete(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteNotExistingVisitByIdTest() throws Exception {
        doThrow(VisitNotFoundException.class).when(visitService).delete(1);
        mvc.perform(delete(PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(visitService, only()).delete(1);
    }

    private VisitResponseDto createVisitResponseDto() {
        return new VisitResponseDto(1, LocalDateTime.of(1999, 7, 12, 10, 0), Duration.ofMinutes(20), "PLANNED", BigDecimal.TEN,
                "", 1, 1, 1, 1);
    }

    private VisitRequestDto creteVisitRequestDto() {
        return new VisitRequestDto(
                LocalDateTime.of(1999, 7, 12, 10, 0),
                Duration.ofMinutes(20), 1, "PLANNED", BigDecimal.TEN, 1, "", 1);
    }
}
