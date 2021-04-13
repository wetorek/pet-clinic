package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.*;
import com.clinic.pet.petclinic.entity.Status;
import com.clinic.pet.petclinic.entity.Vet;
import com.clinic.pet.petclinic.exceptions.ApplicationIllegalArgumentEx;
import com.clinic.pet.petclinic.exceptions.IllegalVisitStateException;
import com.clinic.pet.petclinic.exceptions.VisitNotFoundException;
import com.clinic.pet.petclinic.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class VisitService {
    private final VisitRepository visitRepository;
    private final AnimalRepository animalRepository;
    private final CustomerRepository customerRepository;
    private final SurgeryRepository surgeryRepository;
    private final VetRepository vetRepository;
    private final VisitMapper mapper;
    private final Clock clock;

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
            throw new IllegalVisitStateException("This visit is overlapping");
        }
        if (!checkIfStartTimeIsValid(requestDto.getStartTime())) {
            log.error("Visit cannot be planned on " + requestDto.getStartTime());
            throw new IllegalVisitStateException("The visit cannot be scheduled in the past or for less than an hour");
        }
        var customer = customerRepository.findById(requestDto.getCustomerID())
                .orElseThrow(() -> new ApplicationIllegalArgumentEx("Customer does not exist"));
        var animal = animalRepository.findById(requestDto.getAnimalId())
                .orElseThrow(() -> new ApplicationIllegalArgumentEx("Animal does not exist"));
        var vet = vetRepository.findById(1)
                .orElseThrow(() -> new ApplicationIllegalArgumentEx("Vet does not exist"));
        var visit = mapper.mapToEntity(requestDto, animal, vet, customer);
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

    public VisitResponseDto changeDescription(int id, VisitSetDescriptionRequestDto requestDto) {
        var visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id));
        if ((visit.getStatus() != Status.FINISHED) && (visit.getStatus() != Status.NOT_APPEARED)) {
            throw new IllegalVisitStateException("Visit state restricts changing description");
        }
        visit.setDescription(requestDto.getDescription());
        var created = visitRepository.save(visit);
        return mapper.mapToDto(created);
    }

    public VisitResponseDto changeVisitStatus(int id, VisitSetStatusRequestDto requestDto) {
        var visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id));
        var status = mapper.mapStringToStatus(requestDto.getStatus());
        if ((status != Status.FINISHED) && (status != Status.NOT_APPEARED)) {
            throw new IllegalVisitStateException("Visit state restricts changing status");
        }
        visit.setStatus(status);
        var created = visitRepository.save(visit);
        return mapper.mapToDto(created);
    }

    private boolean checkIfVisitOverlaps(VisitRequestDto requestDto) {
        LocalDateTime endTime = requestDto.getStartTime().plus(requestDto.getDuration());
        return !visitRepository.existOverlapping(requestDto.getStartTime(), endTime).isEmpty();
    }

    private boolean checkIfStartTimeIsValid(LocalDateTime visitStartTime) {
        LocalDateTime currentDateTime = LocalDateTime.now(clock);
        return !visitStartTime.isBefore(currentDateTime) &&
                !visitStartTime.isBefore(currentDateTime.plus(Duration.ofHours(1)));
    }

    public List<FreeSlotVisitResponseDto> findFreeSlots(LocalDateTime start, LocalDateTime end) {
        List<Vet> vets = vetRepository.findAll();
        List<FreeSlotVisitResponseDto> result = new ArrayList<>();

        for (Vet vet : vets){
            LocalDateTime slotTime = start;
            while (slotTime.isBefore(end)){
                if (visitRepository.existVisitBetweenTime(vet.getId(), slotTime, slotTime.plusMinutes(15)).isEmpty()){
                    result.add(new FreeSlotVisitResponseDto(slotTime, vet.getName(), vet.getSurname()));
                }
                slotTime = slotTime.plusMinutes(15);
            }
        }
        return result;
    }
}