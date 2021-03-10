package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Visit;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    VisitResponseDto mapToDto(Visit visit);

    List<VisitResponseDto> mapListToDto(Collection<Visit> visits);
}
