package com.clinic.pet.petclinic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    @NotNull
    private String username;
    @NotNull
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;
    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountState accountState;

    public User(String username, String password, Role role, AccountState accountState) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.accountState = accountState;
    }
}
