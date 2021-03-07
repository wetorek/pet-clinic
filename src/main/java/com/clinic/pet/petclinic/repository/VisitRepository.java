package com.clinic.pet.petclinic.repository;

import com.clinic.pet.petclinic.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Query("select v from visits v where (v.startTime <= :timeFrom and (v.startTime + v.duration) > :timeFrom) or (v.startTime < :timeTo and (v.startTime + v.duration) >= :timeTo)")
    boolean existOverlapping(LocalDateTime timeFrom, LocalDateTime timeTo);

}
