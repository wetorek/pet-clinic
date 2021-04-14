package com.clinic.pet.petclinic.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Table(name = "vets")
@Entity
@Data
@AllArgsConstructor
public class Vet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private LocalTime availabilityFrom;
    @NotNull
    private LocalTime availabilityTo;
    @Lob
    private Byte[] image;

    @OneToMany(mappedBy = "vet")
    private List<Visit> visitList;

    @PersistenceConstructor
    public Vet() {
    }
}
