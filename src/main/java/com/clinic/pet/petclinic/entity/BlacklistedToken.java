package com.clinic.pet.petclinic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "black_listed_tokens")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistedToken {
    @Id
    private String jwt;
}
