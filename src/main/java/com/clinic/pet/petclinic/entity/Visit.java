package com.clinic.pet.petclinic.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Table(name = "visits")
@Entity
@Data
@AllArgsConstructor
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    @Column(columnDefinition = "interval")
    private Duration duration;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @NotNull
    private BigDecimal price;
    private String description;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @ManyToOne
    @JoinColumn(name = "surgery_id", nullable = false)
    private Surgery surgery;

    @PersistenceConstructor
    protected Visit() {
    }
}
