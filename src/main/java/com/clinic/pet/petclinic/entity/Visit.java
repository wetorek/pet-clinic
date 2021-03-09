package com.clinic.pet.petclinic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;


@Entity(name = "visits")
@AllArgsConstructor
@Data
@Builder
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDateTime startTime;
    //Duration is in minutes
    private long duration;
    @Enumerated(EnumType.STRING)
    private Animal animal;
    @Enumerated(EnumType.STRING)
    private Status status;
    private BigDecimal price;

    @PersistenceConstructor
    protected Visit() {
    }

    public static Visit from(LocalDateTime startTime, long duration, String animal, String status, BigDecimal price) {
        var animalEnum = Animal.valueOf(animal);
        var statusEnum = Status.valueOf(status);
        return Visit.builder()
                .startTime(startTime)
                .duration(duration)
                .animal(animalEnum)
                .status(statusEnum)
                .price(price)
                .build();
    }

    public Duration getDuration() {
        return Duration.ofMinutes(duration);
    }
}
