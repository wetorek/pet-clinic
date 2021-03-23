package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.entity.AnimalSpecies;
import com.clinic.pet.petclinic.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    @Mappings({
            @Mapping(target = "id", source = "animal.id"),
            @Mapping(target = "ownerId", source = "owner.id")
    })
    AnimalResponseDto mapToDto(Animal animal);

    List<AnimalResponseDto> mapListToDto(Collection<Animal> animals);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "requestDto.name"),
            @Mapping(target = "owner", source = "owner"),
            @Mapping(target = "visitList", ignore = true)
    })
    Animal mapToEntity(AnimalRequestDto requestDto, Customer owner);

    default AnimalSpecies mapStringToSpecies(String species) {
        return AnimalSpecies.valueOf(species);
    }
}