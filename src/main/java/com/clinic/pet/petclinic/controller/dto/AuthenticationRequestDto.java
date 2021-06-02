package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthenticationRequestDto {
    @NotEmpty(message = "Username is mandatory")
    private String username;
    @NotEmpty(message = "Password is mandatory")
    private String password;
}
