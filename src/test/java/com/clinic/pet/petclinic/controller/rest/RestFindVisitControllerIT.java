package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.FreeSlotVisitResponseDto;
import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.service.FindVisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RestFindVisitControllerIT {
    private static final LocalDateTime LOCAL_DATE_TIME_1 = LocalDateTime.of(2021, 7, 12, 10, 0);
    private static final LocalDateTime LOCAL_DATE_TIME_2 = LocalDateTime.of(2021, 7, 12, 15, 0);
    private final String PATH = "/api/v1/timeslots/find";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FindVisitService findVisitService;

    @Test
    void getAllTimeSlots() throws Exception {
        var vetResponseDto = new VetResponseDto(1, "Walt", "Kowalski", "walt123",
                LocalTime.of(8, 0), LocalTime.of(16, 0));
        var timeslots = List.of(
                new FreeSlotVisitResponseDto(LOCAL_DATE_TIME_1, vetResponseDto)
        );
        when(findVisitService.findFreeSlots(any(), any())).thenReturn(timeslots);

        mockMvc
                .perform(
                        get(PATH)
                                .queryParam("start", LOCAL_DATE_TIME_1.toString())
                                .queryParam("end", LOCAL_DATE_TIME_2.toString())
                                .accept("application/hal+json")
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("_embedded.freeSlotVisitResponseDtoList[0].vet.name", is("Walt")))
                .andExpect(jsonPath("_embedded.freeSlotVisitResponseDtoList[0].vet.surname", is("Kowalski")))
                .andExpect(jsonPath("_embedded.freeSlotVisitResponseDtoList[0].vet.id", is(1)));

        verify(findVisitService, times(1)).findFreeSlots(LOCAL_DATE_TIME_1, LOCAL_DATE_TIME_2);
    }
}