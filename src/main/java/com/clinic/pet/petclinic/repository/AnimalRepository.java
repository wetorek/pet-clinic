package com.clinic.pet.petclinic.repository;

import com.clinic.pet.petclinic.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {

}
