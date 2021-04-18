package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class VetResponseDto {
    private int id;
    private String name;
    private String surname;
    private LocalTime availabilityFrom;
    private LocalTime availabilityTo;
}
