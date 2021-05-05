package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class VisitSetStatusRequestDto {
    @NotBlank(message = "Status is required")
    private String status;
}
