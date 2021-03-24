package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitResponseDto {
    private int id;
    private LocalDateTime startTime;
    private Duration duration;
    private String animal;
    private String status;
    private BigDecimal price;
    private String description;
}
