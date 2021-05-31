package com.clinic.pet.petclinic.controller.dto;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Data
public class VetRequestDto {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "surname is required")
    private String surname;
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
    @NotNull
    private LocalTime availabilityFrom;
    @NotNull
    private LocalTime availabilityTo;
    @Lob
    private Byte[] image;
}
