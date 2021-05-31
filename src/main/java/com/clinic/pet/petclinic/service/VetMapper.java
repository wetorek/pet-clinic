package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VetRequestDto;
import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Role;
import com.clinic.pet.petclinic.entity.Vet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VetMapper {
    VetResponseDto mapToDto(Vet vet);

    List<VetResponseDto> mapListToDto(Collection<Vet> visits);

    @Mappings({
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "accountState", target = "accountState"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "visitList", ignore = true)
    })
    Vet mapToEntity(VetRequestDto requestDto, Role role, AccountState accountState, String password);
}
