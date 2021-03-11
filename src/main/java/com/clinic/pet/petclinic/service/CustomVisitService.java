package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
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
    private final VisitMapper mapper;

    public List<VisitResponseDto> getAllVisits() {
        log.info("Getting all Visits");
        var visits = visitRepository.findAll();
        return mapper.mapListToDto(visits);
    }

    public Optional<VisitResponseDto> getVisitById(int id) {
        log.info("Getting Visit by id: {}", id);
        return visitRepository.findById(id)
                .map(mapper::mapToDto);
    }

    public VisitResponseDto createVisit(VisitRequestDto requestDto) {
        log.info("Creating a Visit");
        if (checkIfVisitOverlaps(requestDto)) {
            log.error("Visit is overlapping");
            throw new IllegalStateException("This visit is overlapping");
        }
        var visit = Visit.from(requestDto.getStartTime(), requestDto.getDuration(), requestDto.getAnimal(), requestDto.getStatus(), requestDto.getPrice());
        var createdVisit = visitRepository.save(visit);
        log.info("Visit created id: {}", createdVisit.getId());
        return mapper.mapToDto(createdVisit);
    }

    public void delete(int id) {
        if (!visitRepository.existsById(id)) {
            log.error("Visit is not found: {}", id);
            throw new VisitNotFoundException("Visit not found: " + id);
        }
        visitRepository.deleteById(id);
    }

    private boolean checkIfVisitOverlaps(VisitRequestDto requestDto) {
        LocalDateTime endTime = requestDto.getStartTime().plus(requestDto.getDuration());
        return !visitRepository.existOverlapping(requestDto.getStartTime(), endTime).isEmpty();
    }

    /*private boolean checkifvisitoverlaps(VisitRequestDto requestDto) { //todo stara wersja z pobraniem wszystkich wizyt
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
    }*/
}