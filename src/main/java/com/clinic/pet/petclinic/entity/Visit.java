package com.clinic.pet.petclinic.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;


@Entity(name = "visits")
@Data
@AllArgsConstructor
@Builder
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    @Column(columnDefinition = "interval")
    private Duration duration;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AnimalSpecies animal;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @NotNull
    private BigDecimal price;
    private String description;
    @NotNull
    @Min(value = 1)
    private int customerID;

    @PersistenceConstructor
    protected Visit() {
    }

    public static Visit from(LocalDateTime startTime, Duration duration, String animal, String status, BigDecimal price, int customerID) {
        var animalEnum = AnimalSpecies.valueOf(animal);
        var statusEnum = Status.valueOf(status);
        return Visit.builder()
                .startTime(startTime)
                .duration(duration)
                .animal(animalEnum)
                .status(statusEnum)
                .price(price)
                .description("")
                .customerID(customerID)
                .build();
    }
}
