package com.clinic.pet.petclinic.controller.dto;

import com.sun.istack.NotNull;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

public class VetRequestDto {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "surname is required")
    private String surname;
    @NotNull
    private LocalTime availabilityFrom;
    @NotNull
    private LocalTime availabilityTo;
    @Lob
    private Byte[] image;
}
