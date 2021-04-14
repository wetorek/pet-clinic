package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalResponseDto extends RepresentationModel<AnimalResponseDto> {
    private int id;
    private String name;
    private LocalDate dateOfBirth;
    private String species;
    private int ownerId;
}
