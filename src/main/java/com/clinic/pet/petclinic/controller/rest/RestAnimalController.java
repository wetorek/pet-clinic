package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    public CollectionModel<AnimalResponseDto> getAllAnimals() {
        var animals = animalService.getAllAnimals();
        var listOfAnimals = animals.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollection(listOfAnimals);
    }

    @GetMapping(path = "/{id}")
    @PostAuthorize(
            "hasAnyRole('VET', 'ADMIN') OR " +
                    "(hasRole('CLIENT') AND ((returnObject.statusCode.value() == 404) OR (returnObject.body.ownerId == authentication.principal.userId)))")
    public ResponseEntity<AnimalResponseDto> getAnimal(@PathVariable @Min(1) int id) {
        return animalService.getAnimalById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/customer")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENT') AND #customerId == authentication.principal.userId)")
    public CollectionModel<AnimalResponseDto> getAnimalsByCustomer(@RequestParam @Min(1) int customerId) {
        var result = animalService.getAnimalsByCustomerId(customerId)
                .stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollection(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENT') AND (#animalRequestDto.ownerID == authentication.principal.userId)")
    AnimalResponseDto createAnimal(@Valid @RequestBody AnimalRequestDto animalRequestDto) {
        var animal = animalService.createAnimal(animalRequestDto);
        return represent(animal);
    }

    private AnimalResponseDto represent(AnimalResponseDto animal) {
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAnimal(animal.getId())).withSelfRel();
        var allAnimals = linkTo(methodOn(RestAnimalController.class).getAllAnimals()).withRel("allAnimals");
        var owner = linkTo(methodOn(RestCustomerController.class).getCustomer(animal.getOwnerId())).withRel("owner");
        return new AnimalResponseDto(animal.getId(), animal.getName(), animal.getDateOfBirth(),
                animal.getSpecies(), animal.getOwnerId()).add(selfLink, allAnimals, owner);
    }

    private CollectionModel<AnimalResponseDto> representCollection(Collection<AnimalResponseDto> animalResponseDtos) {
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAllAnimals()).withSelfRel();
        var allOwners = linkTo(methodOn(RestCustomerController.class).getAllCustomers()).withRel("allOwners");
        return CollectionModel.of(animalResponseDtos, selfLink, allOwners);
    }
}
