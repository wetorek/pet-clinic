package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class VisitResponseDto extends RepresentationModel<VisitResponseDto> {
    private int id;
    private LocalDateTime startTime;
    private Duration duration;
    private String status;
    private BigDecimal price;
    private String description;
    private int animalId;
    private int customerId;
    private int vetId;
    private int surgeryId;
}
