package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.entity.Status;
import com.clinic.pet.petclinic.entity.Visit;
import com.clinic.pet.petclinic.exceptions.VisitNotFoundException;
import com.clinic.pet.petclinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class CustomVisitService implements VisitService {
    private final VisitRepository visitRepository;

    public List<Visit> getAllVisits() {
        log.info("Getting all Visits");
        return visitRepository.findAll();
    }

    public Optional<Visit> getVisitById(int id) {
        log.info("Getting Visit by id: {}", id);
        return visitRepository.findById(id);
    }

    public Visit createVisit(VisitRequestDto requestDto) {
        log.info("Creating a Visit");
        if (checkIfVisitOverlaps(requestDto)) {
            log.error("Visit is overlapping");
            throw new IllegalStateException("This visit is overlapping");
        }
        var visit = Visit.from(requestDto.getStartTime(), requestDto.getDurationInLong(), requestDto.getAnimal(), requestDto.getStatus(), requestDto.getPrice());
        var createdVisit = visitRepository.save(visit);
        log.info("Visit created id: {}", createdVisit.getId());
        return createdVisit;
    }

    public void delete(int id) {
        if (!visitRepository.existsById(id)) {
            log.error("Visit is not found: {}", id);
            throw new VisitNotFoundException("Visit not found: " + id);
        }
        visitRepository.deleteById(id);
    }

    public Visit updateVisit(int id, VisitRequestDto requestDto) {
        if (!visitRepository.existsById(id)) {
            log.error("Visit is not found: {}", id);
            throw new VisitNotFoundException("Visit not found: " + id);
        }
        Visit visit = visitRepository.getOne(id);
        changeTimeVisit(visit, requestDto);
        changeStatus(visit,requestDto);
        changeAnimal(visit, requestDto);
        changePrice(visit, requestDto);

        return visit;
    }

    private void changePrice(Visit visit, VisitRequestDto requestDto) {
        if (visit.getPrice().equals(requestDto.getPrice())){
            visit.setPrice(requestDto.getPrice());
        }
    }

    private void changeAnimal(Visit visit, VisitRequestDto requestDto) {
        if (!visit.getAnimal().toString().equals(requestDto.getAnimal())){
            visit.setAnimal(Animal.valueOf(requestDto.getAnimal()));
        }
    }

    private void changeTimeVisit(Visit visit, VisitRequestDto requestDto){
        if (visit.getStartTime() != requestDto.getStartTime()) {
            if (!checkIfVisitOverlaps(requestDto)){
                visit.setStartTime(requestDto.getStartTime());
            }else{
                log.error("Visit is overlapping");
                throw new IllegalStateException("This visit is overlapping");
            }
        }
        if (visit.getDuration() != requestDto.getDuration()) {
            if (!checkIfVisitOverlaps(requestDto)){
                visit.setDuration(requestDto.getDurationInLong());
            }else {
                log.error("Visit is overlapping");
                throw new IllegalStateException("This visit is overlapping");
            }
        }
    }

    private void changeStatus(Visit visit, VisitRequestDto requestDto) {
        if (!visit.getStatus().toString().equals(requestDto.getStatus())) {
            if (!correctStatus(visit.getStatus(), Status.valueOf(requestDto.getStatus()))){
                log.error("Status cannot be changed");
                throw new IllegalStateException("Status cannot be changed");
            }else {
                visit.setStatus(Status.valueOf(requestDto.getStatus()));
            }
        }
    }

    private boolean checkIfVisitOverlaps(VisitRequestDto requestDto) {
        LocalDateTime endTime = requestDto.getStartTime().plus(requestDto.getDuration());
        return visitRepository.existOverlapping(requestDto.getStartTime(), endTime);
    }

    private boolean checkifvisitoverlaps(VisitRequestDto requestDto) { //todo stara wersja z pobraniem wszystkich wizyt
        List<Visit> visits = visitRepository.findAll();
        LocalDateTime endTime = requestDto.getStartTime().plus(requestDto.getDuration());
        return visits.stream()
                .allMatch(visit -> testVisitIfValid(visit, requestDto.getStartTime(), endTime));
    }

    private boolean testVisitIfValid(Visit visit, LocalDateTime startTime, LocalDateTime endTime) {
        if (visit.getStartTime().isBefore(startTime) && visit.getStartTime().plus(visit.getDuration()).isAfter(startTime)) {
            return false;
        }
        if (visit.getStartTime().isBefore(endTime) && visit.getStartTime().plus(visit.getDuration()).isAfter(endTime)) {
            return false;
        }
        return true;
    }

    private boolean correctStatus(Status status, Status newStatus) {
        if ((status.equals(Status.FINISHED) || status.equals(Status.NOT_APPEARED)) && newStatus.equals(Status.PLANNED)) {
            return false;
        } else if (status.equals(Status.NOT_APPEARED) && newStatus.equals(Status.FINISHED)) {
            return false;
        } else if (status.equals(Status.FINISHED) && newStatus.equals(Status.NOT_APPEARED)) {
            return false;
        } else if (status.equals(newStatus)) {
            return false;
        }
        return true;
    }

}