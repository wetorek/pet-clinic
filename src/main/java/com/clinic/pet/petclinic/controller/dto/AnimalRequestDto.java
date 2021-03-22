package com.clinic.pet.petclinic.controller.dto;

import com.clinic.pet.petclinic.entity.AnimalSpecies;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
public class AnimalRequestDto {

    @NotNull
    private final String name;
    private final LocalDate dateOfBirth;
    @NotNull
    private final AnimalSpecies animalSpecies;
    @NotNull
    @Min(value = 1)
    private final int ownerID;

}
