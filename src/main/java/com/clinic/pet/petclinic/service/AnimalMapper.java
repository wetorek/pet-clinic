package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnimalMapper {

    @Mappings({
            @Mapping(target = "id", source = "animal.id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "dateOfBirth", source = "dateOfBirth"),
            @Mapping(target = "animalSpecies", source = "animalSpecies")
//            @Mapping(target = "ownerName", source = ""),
//            @Mapping(target = "ownerSurname", source = ""),
    })
    AnimalResponseDto mapToDto(Animal animal);
}
