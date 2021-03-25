package com.clinic.pet.petclinic.controller.dto;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CustomerRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
}
