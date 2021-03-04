package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Visit;
import com.clinic.pet.petclinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
@AllArgsConstructor
class VisitController {
    private final VisitMapper mapper;
    private final VisitService visitService;

    @GetMapping
    public List<Visit> getAllVisits() { //todo: map do dto
        return visitService.getAllVisits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponseDto> getVisit(@PathVariable int id) {
        return visitService.getVisitById(id)
                .map(mapper::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<VisitResponseDto> createVisit(@RequestBody VisitRequestDto visitRequestDto) {
        var result = visitService.createVisit(visitRequestDto);
        var mapped = mapper.mapToDto(result);
        return new ResponseEntity<>(mapped, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable int id) {
        visitService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
