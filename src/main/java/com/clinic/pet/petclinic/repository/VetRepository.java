package com.clinic.pet.petclinic.repository;

import com.clinic.pet.petclinic.entity.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Integer> {
}
