package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.service.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestAnimalControllerMVCTest {
    private final String PATH = "/api/v1/animals";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AnimalService animalService;

    @Test
    void getAllAnimals() throws Exception {
        when(animalService.getAllAnimals()).thenReturn(List.of(
                new AnimalResponseDto(1, "animal1", LocalDate.of(1999, 7, 12), "CAT", 1)
        ));

        mockMvc
                .perform(get(PATH).accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("animal1")))
                .andExpect(jsonPath("$[0].dateOfBirth", is("1999-07-12")))
                .andExpect(jsonPath("$[0].species", is("CAT")))
                .andExpect(jsonPath("$[0].ownerId", is(1)))
                .andExpect(jsonPath("$[0].links[?(@.rel=='self')].href", hasItem(endsWithIgnoringCase("/api/v1/animals/1"))))
                .andExpect(jsonPath("$[0].links[?(@.rel=='allAnimals')].href", hasItem(endsWithIgnoringCase("/api/v1/animals"))));
        verify(animalService, times(1)).getAllAnimals();
    }

    @Test
    void getExistingAnimalById() throws Exception {
        when(animalService.getAnimalById(1)).thenReturn(Optional.of(
                new AnimalResponseDto(1, "animal1", LocalDate.of(1999, 7, 12), "CAT", 1)
        ));

        mockMvc
                .perform(get(PATH + "/1").accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("animal1")))
                .andExpect(jsonPath("$.dateOfBirth", is("1999-07-12")))
                .andExpect(jsonPath("$.species", is("CAT")))
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$._links.self.href", is(endsWithIgnoringCase("/api/v1/animals/1"))))
                .andExpect(jsonPath("$._links.allAnimals.href", is(endsWithIgnoringCase("/api/v1/animals"))));
        verify(animalService, times(1)).getAnimalById(1);
    }

    @Test
    void getNotExistingAnimal() throws Exception {
        when(animalService.getAnimalById(1)).thenReturn(Optional.empty());

        mockMvc
                .perform(get(PATH + "/1").accept("application/hal+json"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$").doesNotExist());
        verify(animalService, times(1)).getAnimalById(1);
    }

    @Test
    void createAnimal() throws Exception {
        var requestDto = new AnimalRequestDto("animal1", LocalDate.of(1999, 7, 12), "CAT", 1);
        var expected = new AnimalResponseDto(1, "animal1", LocalDate.of(1999, 7, 12), "CAT", 1);
        when(animalService.createAnimal(any())).thenReturn(expected);

        mockMvc
                .perform(
                        post(PATH)
                                .contentType("application/json")
                                .accept("application/hal+json")
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("animal1")))
                .andExpect(jsonPath("$.dateOfBirth", is("1999-07-12")))
                .andExpect(jsonPath("$.species", is("CAT")))
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$._links.self.href", is(endsWithIgnoringCase("/api/v1/animals/1"))))
                .andExpect(jsonPath("$._links.allAnimals.href", is(endsWithIgnoringCase("/api/v1/animals"))));
        verify(animalService, times(1)).createAnimal(requestDto);
    }
}