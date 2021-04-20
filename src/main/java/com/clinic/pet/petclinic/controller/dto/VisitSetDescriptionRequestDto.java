package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class VisitSetDescriptionRequestDto {
    @NotBlank(message = "Description is required")
    private String description;
}
