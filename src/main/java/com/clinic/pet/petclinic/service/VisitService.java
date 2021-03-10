package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;

import java.util.List;
import java.util.Optional;

public interface VisitService {
    List<VisitResponseDto> getAllVisits();

    Optional<VisitResponseDto> getVisitById(int id);

    VisitResponseDto createVisit(VisitRequestDto requestDto);

    void delete(int id);
}
