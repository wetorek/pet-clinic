package com.clinic.pet.petclinic.controller.dto;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class VisitSetDescriptionRequestDto {


    private final String description;
}
