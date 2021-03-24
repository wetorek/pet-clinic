package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetDescriptionRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetStatusRequestDto;
import com.clinic.pet.petclinic.entity.Status;
import com.clinic.pet.petclinic.repository.VetRepository;
import com.clinic.pet.petclinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class VetService {
    private final VetRepository vetRepository;
    private final VisitRepository visitRepository;

    public VisitResponseDto changeDescription(int id, VisitSetDescriptionRequestDto requestDto){ //todo
        var visit= visitRepository.findById(id).get();
        visit.setDescription(requestDto.getDescription());
        visitRepository.save(visit);
        return new VisitResponseDto(visit.getId(), visit.getStartTime(), visit.getDuration(),
                visit.getAnimal().toString(), visit.getStatus().toString(), visit.getPrice(), visit.getDescription());
    }

    public VisitResponseDto changeVisitStatus(int id, VisitSetStatusRequestDto requestDto){ //todo
        var visit = visitRepository.findById(id).get();
        Status status = Status.valueOf(requestDto.getStatus());
        if (status.equals(Status.FINISHED) || status.equals(Status.NOT_APPEARED)){
            visit.setStatus(status);
            visitRepository.save(visit);
        }
        return new VisitResponseDto(visit.getId(), visit.getStartTime(), visit.getDuration(),
                visit.getAnimal().toString(), visit.getStatus().toString(), visit.getPrice(), visit.getDescription());
    }
}
