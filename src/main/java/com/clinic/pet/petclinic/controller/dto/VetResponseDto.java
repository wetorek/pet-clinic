package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class VetResponseDto extends RepresentationModel<VetResponseDto> {
    private int id;
    private String name;
    private String surname;
    private String username;
    private LocalTime availabilityFrom;
    private LocalTime availabilityTo;
}
