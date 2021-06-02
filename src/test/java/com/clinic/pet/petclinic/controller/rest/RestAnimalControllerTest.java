package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.service.AnimalService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
class RestAnimalControllerTest {
    @Mock
    private AnimalService animalService;
    @InjectMocks
    private RestAnimalController animalController;

    @Test
    void getAllAnimals() {
        var animalResponse = createAnimalResponse();
        when(animalService.getAllAnimals()).thenReturn(List.of(animalResponse));
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAnimal(1)).withSelfRel();
        var allVets = linkTo(methodOn(RestVetController.class).getAllVets()).withRel("allVets");
        var actual = animalController.getAllAnimals();

        assertThat(actual).containsExactly(animalResponse);
        assertThat(actual.getContent().iterator().next())
                .extracting(u -> u.getLinks().toList(), InstanceOfAssertFactories.LIST)
                .containsExactlyInAnyOrder(selfLink, allVets);
        verify(animalService, times(1)).getAllAnimals();
    }

    @Test
    void getExistingAnimalById() {
        var animalResponse = createAnimalResponse();
        when(animalService.getAnimalById(1)).thenReturn(Optional.of(animalResponse));
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAnimal(1)).withSelfRel();
        var allVets = linkTo(methodOn(RestVetController.class).getAllVets()).withRel("allVets");
        var actual = animalController.getAnimal(1);

        assertThat(actual).extracting(HttpEntity::getBody).isEqualTo(animalResponse);
        assertThat(actual.getBody()).extracting(u -> u.getLinks().toList(), InstanceOfAssertFactories.LIST)
                .containsExactlyInAnyOrder(selfLink, allVets);
        verify(animalService, times(1)).getAnimalById(1);
    }

    @Test
    void getNotExistingAnimal() {
        when(animalService.getAnimalById(1)).thenReturn(Optional.empty());

        var actual = animalController.getAnimal(1);

        assertThat(actual).extracting(ResponseEntity::getStatusCodeValue).isEqualTo(404);
    }

    @Test
    void createAnimal() {
        var requestDto = new AnimalRequestDto("animal1", LocalDate.of(1999, 7, 12), "CAT", 1);
        var expected = new AnimalResponseDto(1, "animal1", LocalDate.of(1999, 7, 12), "CAT", 1);
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAnimal(1)).withSelfRel();
        var allVets = linkTo(methodOn(RestVetController.class).getAllVets()).withRel("allVets");
        when(animalService.createAnimal(any())).thenReturn(expected);

        var actual = animalController.createAnimal(requestDto);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getLinks().toList()).containsExactlyInAnyOrder(selfLink, allVets);
        verify(animalService, times(1)).createAnimal(requestDto);
    }

    private AnimalResponseDto createAnimalResponse() {
        return new AnimalResponseDto(1, "animal1", LocalDate.of(1999, 7, 12), "CAT", 1);
    }
}