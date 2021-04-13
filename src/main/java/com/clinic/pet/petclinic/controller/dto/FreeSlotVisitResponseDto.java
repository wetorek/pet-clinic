package com.clinic.pet.petclinic.controller.dto;

import com.clinic.pet.petclinic.entity.Surgery;
import com.clinic.pet.petclinic.entity.Vet;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class FreeSlotVisitResponseDto {
    private LocalDateTime start;
    private Vet vet;


    public FreeSlotVisitResponseDto(LocalDateTime slotTime, Vet vet) {
        this.start = slotTime;
        this.vet = vet;
    }
}
