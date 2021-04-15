package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.entity.Vet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VetMapper {

    @Mappings({
            @Mapping(target = "id", source = "id" ),
            @Mapping(target = "name", source = "name" ),
            @Mapping(target = "surname", source = "surname" )
    })
    VetResponseDto mapToDto(Vet vet);
}
