package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Visit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VisitMapper {

    public VisitResponseDto mapToDto(Visit visit){
        return null;
    }

    public List<VisitResponseDto> mapListToDto(List<Visit> visits){
        return null;
    }
}
