package com.clinic.pet.petclinic.controller.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class VisitResponseDto {
    private final int id;
    private final LocalDateTime startTime;
    private final Duration duration;
    private final String animal;
    private final String status;
    private final BigDecimal price;
}
