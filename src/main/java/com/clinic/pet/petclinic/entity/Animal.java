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
    private AnimalSpecies animalSpecies;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Customer owner;

    @OneToMany(mappedBy = "animal")
    private List<Visit> visitList;

    @PersistenceConstructor
    public Animal() {
    }

//    public static Animal from(String name, LocalDate dateOfBirth, String animalSpecies, int ownerID) {
//        var animalEnum = AnimalSpecies.valueOf(animalSpecies);
//        return Animal.builder()
//                .name(name)
//                .dateOfBirth(dateOfBirth)
//                .animalSpecies(animalEnum)
//                .ownerID(ownerID)
//                .build();
//    }
}
