package com.clinic.pet.petclinic.repository;

import com.clinic.pet.petclinic.entity.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurgeryRepository extends JpaRepository<Surgery, Integer> {
}
