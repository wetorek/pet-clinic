package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class VisitRequestDto {
    private final LocalDateTime startTime;
    private final Duration duration;
    private final String animal;
    private final String status;
    private final BigDecimal price;
}
