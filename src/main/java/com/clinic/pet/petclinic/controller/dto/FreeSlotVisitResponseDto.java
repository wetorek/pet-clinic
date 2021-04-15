package com.clinic.pet.petclinic.controller.dto;

import com.clinic.pet.petclinic.entity.Surgery;
import com.clinic.pet.petclinic.entity.Vet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeSlotVisitResponseDto extends RepresentationModel<FreeSlotVisitResponseDto> {
    private LocalDateTime start;
    private VetResponseDto vet;
}
