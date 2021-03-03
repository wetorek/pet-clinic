package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Status;
import com.clinic.pet.petclinic.entity.Visit;
import com.clinic.pet.petclinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//@AllArgsConstructor
@Service
public class VisitService {

    private VisitRepository visitRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public boolean delete(int id) {
        var visit = visitRepository.findById(id);
        if (visit.isPresent()) {
            visitRepository.deleteById(visit.get().getId());
            return true;
        } else {
            return false;
        }
    }

    public boolean createVisit(VisitResponseDto newVisit){
        if (availableVisit(newVisit)){
            visitRepository.save(Visit.from(newVisit.getStartTime(),
                                            newVisit.getDuration(),
                                            newVisit.getAnimal(),
                                            newVisit.getStatus(),
                                            newVisit.getPrice()));
            return true;
        }
        return false;
    }

    private boolean availableVisit(VisitResponseDto newVisit) {
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