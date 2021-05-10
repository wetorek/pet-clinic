package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VetRequestDto;
import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.entity.Vet;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VetMapper {
    VetResponseDto mapToDto(Vet vet);

    List<VetResponseDto> mapListToDto(Collection<Vet> visits);

    Vet mapToEntity(VetRequestDto requestDto);
}
