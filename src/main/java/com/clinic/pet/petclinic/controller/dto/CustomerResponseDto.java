package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto extends RepresentationModel<CustomerResponseDto> {
    private int id;
    private String name;
    private String surname;
}
