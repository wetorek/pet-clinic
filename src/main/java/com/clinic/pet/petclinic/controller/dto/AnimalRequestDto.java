package com.clinic.pet.petclinic.controller.dto;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class AnimalRequestDto {
    @NotNull
    private  String name;
    private  LocalDate dateOfBirth;
    @NotBlank
    private  String species;
    @NotNull
    @Min(value = 1)
    private  int ownerID;

}
