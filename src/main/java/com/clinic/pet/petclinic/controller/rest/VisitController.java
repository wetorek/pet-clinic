package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Visit;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@AllArgsConstructor
class VisitController {
    private final ModelMapper mapper;

    @GetMapping
    List<VisitResponseDto> getAll() {
        return null;
    }

    private VisitResponseDto mapToDto(Visit visit) {
        return mapper.map(visit, VisitResponseDto.class);
    }

}
