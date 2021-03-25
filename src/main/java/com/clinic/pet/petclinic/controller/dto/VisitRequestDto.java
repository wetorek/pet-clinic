package com.clinic.pet.petclinic.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.convert.DurationUnit;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class VisitRequestDto {
    private final LocalDateTime startTime;
    @DurationUnit(ChronoUnit.MINUTES)
    @DurationMin(minutes = 1)
    @DurationMax(hours = 6)
    private final Duration duration;
//    @NotBlank(message = "Animal type is required")
    @Min(1)
    private final int animalId;
    @NotBlank(message = "Visit status is required")
    private final String status;
    @DecimalMin("1")
    @DecimalMax("10000")
    private final BigDecimal price;
    @Min(value = 1)
    private final int customerID;
    private String description;
}
