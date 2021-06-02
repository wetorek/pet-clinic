package com.clinic.pet.petclinic.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Table(name = "customers")
@Entity
@Data
@AllArgsConstructor
public class Customer extends User {
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

    //only for tests
    public Customer(Integer id, String name, String surname) {
        super(id, null, null, Role.ROLE_CLIENT, AccountState.ACTIVE);
        this.name = name;
        this.surname = surname;
    }

    public Customer(Integer id, String name, String surname, String username) {
        super(id, username, null, Role.ROLE_CLIENT, AccountState.ACTIVE);
        this.name = name;
        this.surname = surname;
    }

    public Customer(Integer id, String username, String password, Role role, AccountState accountState, String name, String surname) {
        super(id, username, password, role, accountState);
        this.name = name;
        this.surname = surname;
    }
}
