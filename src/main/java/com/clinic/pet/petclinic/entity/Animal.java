package com.clinic.pet.petclinic.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Table(name = "animals")
@Entity
@Data
@AllArgsConstructor
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name;
    private LocalDate dateOfBirth;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AnimalSpecies species;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Customer owner;

    @OneToMany(mappedBy = "animal")
    private List<Visit> visitList;

    @PersistenceConstructor
    public Animal() {
    }
}
