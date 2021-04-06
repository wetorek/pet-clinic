package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class AnimalRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    private LocalDate dateOfBirth;
    @NotBlank(message = "Species is required")
    private String species;
    @Min(value = 1)
    private int ownerID;

}
