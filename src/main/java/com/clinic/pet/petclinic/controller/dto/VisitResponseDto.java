package com.clinic.pet.petclinic.controller.dto;

import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.entity.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class VisitResponseDto {
    private final int id;
    private final LocalDateTime startTime;
    private final Duration duration;
    private final Animal animal;
    private final Status status;
    private final BigDecimal price;
}
