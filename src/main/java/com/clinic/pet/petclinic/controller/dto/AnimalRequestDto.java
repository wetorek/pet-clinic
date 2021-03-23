package com.clinic.pet.petclinic.controller.dto;

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
    private final String species;
    @NotNull
    @Min(value = 1)
    private final int ownerID;

}
