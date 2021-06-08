package com.clinic.pet.petclinic.repository;

import com.clinic.pet.petclinic.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Query("select v from Visit v where (v.startTime <= :timeFrom and (v.startTime + v.duration) > :timeFrom) or (v.startTime < :timeTo and (v.startTime + v.duration) >= :timeTo)")
    List<Visit> existOverlapping(LocalDateTime timeFrom, LocalDateTime timeTo);

    @Modifying
    @Query("update Visit  v set v.status ='FINISHED_AUTOMATICALLY'  where (v.startTime + v.duration) <= :dateTime and v.status = 'PLANNED' ")
    @Transactional
    void automaticEndingVisits(@Param("dateTime") LocalDateTime dateTime);

    @Query("select v from Visit v where(v.vet.id = :id and ( (v.startTime >= :startTime and v.startTime <= :endTime) or ( (v.startTime + v.duration) >= :startTime and (v.startTime + v.duration) <= :endTime  ) ) )")
    List<Visit> existVisitBetweenTime(@Param("id") Integer id, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("select v from Visit v where v.vet.id = :id")
    List<Visit> vetsVisits(@Param("id") Integer id);

    List<Visit> getAllByCustomerId(int id);
}
