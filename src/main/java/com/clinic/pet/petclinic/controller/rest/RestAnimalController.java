package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/animals", produces = "application/hal+json")
@AllArgsConstructor
public class RestAnimalController {
    private final AnimalService animalService;

    @GetMapping
    public CollectionModel<AnimalResponseDto> getAllAnimals() {
        var animals = animalService.getAllAnimals();
        var listOfAnimals = animals.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollection(listOfAnimals);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AnimalResponseDto> getAnimal(@PathVariable @Min(1) int id) {
        return animalService.getAnimalById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AnimalResponseDto createAnimal(@Valid @RequestBody AnimalRequestDto animalRequestDto) {
        var animal = animalService.createAnimal(animalRequestDto);
        return represent(animal);
    }

    private AnimalResponseDto represent(AnimalResponseDto animal) {
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAnimal(animal.getId())).withSelfRel();
        var allVets = linkTo(methodOn(RestVetController.class).getAllVets()).withRel("allVets");
        return new AnimalResponseDto(animal.getId(), animal.getName(), animal.getDateOfBirth(),
                animal.getSpecies(), animal.getOwnerId()).add(selfLink, allVets);
    }

    private CollectionModel<AnimalResponseDto> representCollection(Collection<AnimalResponseDto> animalResponseDtos) {
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAllAnimals()).withSelfRel();
        return CollectionModel.of(animalResponseDtos, selfLink);
    }
}
