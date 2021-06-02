package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VetRequestDto;
import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.service.VetService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/vets", produces = "application/hal+json")
@AllArgsConstructor
public class RestVetController {
    private final VetService vetService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<VetResponseDto> getAllVets() {
        var vets = vetService.getAllVets();
        var vetResponseDtos = vets.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollection(vetResponseDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('VET') AND #id == authentication.principal.userId)")
    public ResponseEntity<VetResponseDto> getVet(@PathVariable @Min(1) int id) {
        return vetService.getVetById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    VetResponseDto createVet(@RequestBody VetRequestDto vetRequestDto) {
        var vet = vetService.createVet(vetRequestDto);
        return represent(vet);
    }

    private VetResponseDto represent(VetResponseDto responseDto) {
        var selfLink = linkTo(methodOn(RestVetController.class).getVet(responseDto.getId())).withSelfRel();
        var allVets = linkTo(methodOn(RestVetController.class).getAllVets()).withRel("allVets");
        return responseDto.add(selfLink, allVets);
    }

    private CollectionModel<VetResponseDto> representCollection(Collection<VetResponseDto> vetResponseDtos) {
        var selfLink = linkTo(methodOn(RestVetController.class).getAllVets()).withSelfRel();
        return CollectionModel.of(vetResponseDtos, selfLink);
    }
}
