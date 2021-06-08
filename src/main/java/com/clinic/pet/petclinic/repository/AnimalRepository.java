package com.clinic.pet.petclinic.repository;

import com.clinic.pet.petclinic.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {


    List<Animal> getAllByOwnerId(int id);
}
