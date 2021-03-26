package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CustomerRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Surname is required")
    private String surname;
}
