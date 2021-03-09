package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.entity.Visit;

import java.util.List;
import java.util.Optional;

public interface VisitService {
    List<Visit> getAllVisits();

    Optional<Visit> getVisitById(int id);

    Visit createVisit(VisitRequestDto requestDto);

    void delete(int id);

    Visit updateVisit(int id, VisitRequestDto requestDto);
}
