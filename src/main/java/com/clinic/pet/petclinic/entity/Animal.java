package com.clinic.pet.petclinic.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "animals")
@Data
@AllArgsConstructor
@Builder
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String name;

    private LocalDate dateOfBirth;

    private AnimalSpecies animalSpecies;

//    @ForeignKey()
    private int ownerID;

    @PersistenceConstructor
    public Animal() {
    }

    public static Animal from(String name, LocalDate dateOfBirth, String animalSpecies, int ownerID) {
        var animalEnum = AnimalSpecies.valueOf(animalSpecies);
        return Animal.builder()
                .name(name)
                .dateOfBirth(dateOfBirth)
                .animalSpecies(animalEnum)
                .ownerID(ownerID)
                .build();
    }
}
