package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeSlotVisitResponseDto extends RepresentationModel<FreeSlotVisitResponseDto> {
    private LocalDateTime start;
    private VetResponseDto vet;
}
