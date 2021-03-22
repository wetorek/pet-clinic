package com.clinic.pet.petclinic.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name = "customers")
@Entity
@Data
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String surname;

    @OneToMany(mappedBy = "owner")
    private List<Animal> animalList;
    @OneToMany(mappedBy = "customer")
    private List<Visit> visitList;

    @PersistenceConstructor
    public Customer() {
    }
}