package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mappings({
            @Mapping(target = "animalId", source = "animal.id"),
            @Mapping(target = "customerId", source = "customer.id"),
            @Mapping(target = "vetId", source = "vet.id"),
            @Mapping(target = "surgeryId", source = "surgery.id"),
    })
    VisitResponseDto mapToDto(Visit visit);

    List<VisitResponseDto> mapListToDto(Collection<Visit> visits);

    @Mappings({
            @Mapping(source = "animal", target = "animal"),
            @Mapping(source = "vet", target = "vet"),
            @Mapping(source = "customer", target = "customer"),
            @Mapping(source = "surgery", target = "surgery"),
            @Mapping(target = "id", ignore = true)
    })
    Visit mapToEntity(VisitRequestDto requestDto, Animal animal, Vet vet, Customer customer, Surgery surgery);

    default Status mapStringToStatus(String status) {
        return Status.valueOf(status);
    }
}
