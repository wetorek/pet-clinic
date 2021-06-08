package com.clinic.pet.petclinic.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Table(name = "vets")
@Entity
@Data
public class Vet extends User {
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

    public Vet(Integer id, String name, String surname, LocalTime availabilityFrom, LocalTime availabilityTo) {
        super(id, null, null, Role.ROLE_VET, AccountState.ACTIVE);
        this.name = name;
        this.surname = surname;
        this.availabilityFrom = availabilityFrom;
        this.availabilityTo = availabilityTo;
    }

    public Vet(Integer id, String username, String name, String surname, LocalTime availabilityFrom, LocalTime availabilityTo) {
        super(id, username, null, Role.ROLE_VET, AccountState.ACTIVE);
        this.name = name;
        this.surname = surname;
        this.availabilityFrom = availabilityFrom;
        this.availabilityTo = availabilityTo;
    }
}
