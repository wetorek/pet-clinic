package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class VisitRequestDto {
    private final LocalDateTime startTime;
    @Min(1)
    @Max(480)
    private final Duration duration;
    @NotBlank(message = "Animal type is required")
    private final String animal;
    @NotBlank(message = "Visit status is required")
    private final String status;
    @DecimalMin("1")
    @DecimalMax("10000")
    private final BigDecimal price;
}
