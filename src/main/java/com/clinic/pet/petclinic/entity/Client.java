package com.clinic.pet.petclinic.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "clients")
@Data
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String surname;

    @PersistenceConstructor
    public Client() {
    }

    public static Client from(String name, String surname) {
        return Client.builder()
                .name(name)
                .surname(surname)
                .build();
    }
}
