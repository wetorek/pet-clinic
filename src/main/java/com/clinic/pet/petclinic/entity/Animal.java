package com.clinic.pet.petclinic.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Table(name = "animals")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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

    public Animal(Integer id, String name, LocalDate dateOfBirth, AnimalSpecies species, Customer owner) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.species = species;
        this.owner = owner;
    }
}
