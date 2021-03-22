package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mappings({
            @Mapping(target = "animal", source = "animal.id")
    })
    VisitResponseDto mapToDto(Visit visit);

    List<VisitResponseDto> mapListToDto(Collection<Visit> visits);
}
