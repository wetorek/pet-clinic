package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.entity.Status;
import com.clinic.pet.petclinic.entity.Visit;
import com.clinic.pet.petclinic.exceptions.VisitNotFoundException;
import com.clinic.pet.petclinic.repository.VisitRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VisitService {
    private final VisitRepository visitRepository;

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public void delete(int id) {
        if (!visitRepository.existsById(id)){
            throw new VisitNotFoundException("Visit not found: " + id);
        }
        visitRepository.deleteById(id);
    }

    public Visit createVisit(VisitRequestDto requestDto){
        if (!availableVisit(requestDto)){
            throw new IllegalStateException();
        }
        var visit = Visit.from(requestDto.getStartTime(),requestDto.getDuration(), requestDto.getAnimal(), requestDto.getStatus(),
                                     requestDto.getPrice());
        return visitRepository.save(visit);
    }

    private boolean availableVisit(VisitRequestDto newVisit) {
        List<Visit> visits = getAllVisits();
        LocalDateTime startTime = newVisit.getStartTime();
        LocalDateTime endTime = newVisit.getStartTime().plus(newVisit.getDuration());
        for (Visit v : visits){
           if (v.getStartTime().isBefore(startTime) && v.getStartTime().plus(v.getDuration()).isAfter(startTime)){
               return false;
           }
           if (v.getStartTime().isBefore(endTime) && v.getStartTime().plus(v.getDuration()).isAfter(endTime)){
               return false;
           }
        }
        return true;
    }

    public Optional<Visit> getVisitById(int id) {
        return visitRepository.findById(id);
    }

    public boolean changeStatus(int id, Status newStatus) {
        var visit = getVisitById(id);
        if (visit.isPresent()) {
            if (correctStatus(visit.get().getStatus(), newStatus)) {
                visit.get().setStatus(newStatus);
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean correctStatus(Status status, Status newStatus) {
        if ( (status.equals(Status.FINISHED) || status.equals(Status.NOT_APPEARED) ) && newStatus.equals(Status.PLANNED)){
            return false;
        }else if (status.equals(Status.NOT_APPEARED) && newStatus.equals(Status.FINISHED)){
            return false;
        }else if (status.equals(Status.FINISHED) && newStatus.equals(Status.NOT_APPEARED)){
            return false;
        }else if (status.equals(newStatus)){
            return false;
        }
        return true;
    }

}