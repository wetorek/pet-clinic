package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VisitSetStatusRequestDto {
    @NotBlank(message = "Status is required")
    private String status;
}
