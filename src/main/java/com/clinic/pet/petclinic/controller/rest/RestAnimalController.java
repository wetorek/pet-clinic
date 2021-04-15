package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/animals")
@AllArgsConstructor
public class RestAnimalController {
    private final AnimalService animalService;

    @GetMapping(produces = "application/hal+json")
    public List<AnimalResponseDto> getAllAnimals() {
        var animals = animalService.getAllAnimals();
        return animals.stream()
                .map(this::represent)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}", produces = "application/hal+json")
    public ResponseEntity<AnimalResponseDto> getAnimal(@PathVariable @Min(1) int id) {
        return animalService.getAnimalById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    AnimalResponseDto createAnimal(@Valid @RequestBody AnimalRequestDto animalRequestDto) {
        var animal = animalService.addAnimal(animalRequestDto);
        return represent(animal);
    }

    private AnimalResponseDto represent(AnimalResponseDto animal) {
        Link selfLink = linkTo(methodOn(RestAnimalController.class).getAnimal(animal.getId())).withSelfRel();
        Link allAnimals = linkTo(methodOn(RestAnimalController.class).getAllAnimals()).withSelfRel();
        var representation = new AnimalResponseDto(animal.getId(), animal.getName(), animal.getDateOfBirth(), animal.getSpecies(), animal.getOwnerId());
        representation.add(selfLink, allAnimals);
        return representation;
    }

}
