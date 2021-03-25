package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.convert.DurationUnit;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class VisitRequestDto {
    @NotNull
    private LocalDateTime startTime;
    @DurationUnit(ChronoUnit.MINUTES)
    @DurationMin(minutes = 1)
    @DurationMax(hours = 6)
    private Duration duration;
    @Min(1)
    private int animalId;
    @NotBlank(message = "Visit status is required")
    private String status;
    @DecimalMin("1")
    @DecimalMax("10000")
    private BigDecimal price;
    @Min(value = 1)
    private int customerID;
    private String description;
}
