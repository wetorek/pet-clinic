package com.clinic.pet.petclinic.controller.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class CustomerRequestDto {
    @NotNull
    private final String name;
    @NotNull
    private final String surname;
}
