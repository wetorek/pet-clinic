package com.clinic.pet.petclinic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;


@Entity
@Table(name = "visits")
@AllArgsConstructor
@Data
@Setter
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDateTime startTime;
    private Duration duration;
    @Enumerated(EnumType.STRING)
    private Animal animal;
    @Enumerated(EnumType.STRING)
    private Status status;
    private BigDecimal price;

    @PersistenceConstructor
    protected Visit() {
    }

    public static Visit from(LocalDateTime startTime, Duration duration, String animal, String status, BigDecimal price) {
        var animalEnum = Animal.valueOf(animal);
        var statusEnum = Status.valueOf(status);
        return new Visit(0, startTime, duration, animalEnum, statusEnum, price);
    }


}
