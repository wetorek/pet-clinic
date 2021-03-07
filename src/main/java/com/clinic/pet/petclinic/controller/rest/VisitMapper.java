package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Visit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VisitMapper implements Mapper<Visit, VisitResponseDto> {

    public VisitResponseDto mapToDto(Visit visit) {
        return VisitResponseDto.builder()
                .id(visit.getId())
                .duration(visit.getDuration())
                .animal(visit.getAnimal().toString())
                .price(visit.getPrice())
                .startTime(visit.getStartTime())
                .status(visit.getStatus().toString())
                .build();
    }

    public List<VisitResponseDto> mapListToDto(List<Visit> visits) {
        return visits.stream()
                .map(this::mapToDto)
                .collect(Collectors.toUnmodifiableList());
    }
}
