package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.auth.AuthenticatedUser;
import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Role;
import com.clinic.pet.petclinic.service.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.endsWithIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RestAnimalControllerIT {
    private final String PATH = "/api/v1/animals";
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AnimalService animalService;

    @BeforeEach
    void initialize() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAnimals() throws Exception {
        when(animalService.getAllAnimals()).thenReturn(List.of(
                new AnimalResponseDto(1, "animal1", LocalDate.of(1999, 7, 12), "CAT", 1)
        ));

        mockMvc
                .perform(get(PATH).accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("_embedded.animalResponseDtoList[0].id", is(1)))
                .andExpect(jsonPath("_embedded.animalResponseDtoList[0].name", is("animal1")))
                .andExpect(jsonPath("_embedded.animalResponseDtoList[0].dateOfBirth", is("1999-07-12")))
                .andExpect(jsonPath("_embedded.animalResponseDtoList[0].species", is("CAT")))
                .andExpect(jsonPath("_embedded.animalResponseDtoList[0].ownerId", is(1)))
                .andExpect(jsonPath("_links.self.href", is(Matchers.endsWithIgnoringCase("/api/v1/animals"))));
        verify(animalService, times(1)).getAllAnimals();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
                .andExpect(jsonPath("$._links.self.href", is(endsWithIgnoringCase("/api/v1/animals/1"))));
        verify(animalService, times(1)).getAnimalById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
        var clientUser = (UserDetails) new AuthenticatedUser(1, "client", "dsad", AccountState.ACTIVE, Collections.singletonList(Role.ROLE_CLIENT));
        mockMvc
                .perform(
                        post(PATH)
                                .contentType("application/json")
                                .accept("application/hal+json")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .with(user(clientUser))
                )
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("animal1")))
                .andExpect(jsonPath("$.dateOfBirth", is("1999-07-12")))
                .andExpect(jsonPath("$.species", is("CAT")))
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$._links.self.href", is(endsWithIgnoringCase("/api/v1/animals/1"))));
        verify(animalService, times(1)).createAnimal(requestDto);
    }
}