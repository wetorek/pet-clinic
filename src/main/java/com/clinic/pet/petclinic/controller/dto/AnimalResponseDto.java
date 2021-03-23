package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AnimalResponseDto {
    private int id;
    private String name;
    private LocalDate dateOfBirth;
    private String species;
    private int ownerId;
}
