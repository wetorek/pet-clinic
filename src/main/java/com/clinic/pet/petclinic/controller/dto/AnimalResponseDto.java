package com.clinic.pet.petclinic.controller.dto;

import com.clinic.pet.petclinic.entity.AnimalSpecies;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AnimalResponseDto {

    private int id;
    private String name;
    private LocalDate dateOfBirth;
    private AnimalSpecies animalSpecies;
    private String ownerName;
    private String ownerSurname;
}
