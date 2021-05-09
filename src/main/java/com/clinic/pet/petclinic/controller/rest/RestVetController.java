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

@RestController
@RequestMapping(value = "/api/v1/vets", produces = "application/hal+json")
@AllArgsConstructor
public class RestVetController {
    private final VetService vetService;

    @GetMapping
    public List<VetResponseDto> getAllVets() {
        return vetService.getAllVets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetResponseDto> getVet(@PathVariable @Min(1) int id) {
        return vetService.getVetById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    VetResponseDto createVet(@RequestBody VetRequestDto vetRequestDto) {
        return vetService.createVet(vetRequestDto);
    }

}
