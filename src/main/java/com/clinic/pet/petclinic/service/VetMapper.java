package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.entity.Vet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VetMapper {
    VetResponseDto mapToDto(Vet vet);
}
