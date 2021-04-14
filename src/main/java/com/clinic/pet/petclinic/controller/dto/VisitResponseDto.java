package com.clinic.pet.petclinic.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitResponseDto extends RepresentationModel<VisitResponseDto> {
    private int id;
    private LocalDateTime startTime;
    private Duration duration;
    private String animal;
    private String status;
    private BigDecimal price;
    private String description;
}
