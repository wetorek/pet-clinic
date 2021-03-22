package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    @Mappings({
            @Mapping(target = "id", source = "animal.id"),
    })
    AnimalResponseDto mapToDto(Animal animal);

    List<AnimalResponseDto> mapListToDto(Collection<Animal> animals);
}