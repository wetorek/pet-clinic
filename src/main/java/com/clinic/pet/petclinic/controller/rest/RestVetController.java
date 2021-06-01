package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VetRequestDto;
import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.service.VetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/vets", produces = "application/hal+json")
@AllArgsConstructor
public class RestVetController {
    private final VetService vetService;

    @GetMapping
    public List<VetResponseDto> getAllVets() {
        var vets = vetService.getAllVets();
        return vets.stream()
                .map(this::represent)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetResponseDto> getVet(@PathVariable @Min(1) int id) {
        return vetService.getVetById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/vets")
    @ResponseStatus(HttpStatus.CREATED)
    VetResponseDto createVet(@RequestBody VetRequestDto vetRequestDto) {
        var vet = vetService.createVet(vetRequestDto);
        return represent(vet);
    }

    private VetResponseDto represent(VetResponseDto responseDto) {
        var selfLink = linkTo(methodOn(RestVetController.class).getVet(responseDto.getId())).withSelfRel();
        var allVets = linkTo(methodOn(RestVetController.class).getAllVets()).withRel("allVets");
        return responseDto.add(selfLink, allVets);
    }
}
