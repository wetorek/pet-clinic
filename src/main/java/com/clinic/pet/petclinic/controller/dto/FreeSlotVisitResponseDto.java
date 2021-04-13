package com.clinic.pet.petclinic.controller.dto;

import com.clinic.pet.petclinic.entity.Surgery;
import com.clinic.pet.petclinic.entity.Vet;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
public class FreeSlotVisitResponseDto {
    private LocalDateTime start;
    private String vetName;
    private String vetSurname;

}
