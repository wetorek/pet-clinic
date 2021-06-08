package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetDescriptionRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetStatusRequestDto;
import com.clinic.pet.petclinic.entity.Status;
import com.clinic.pet.petclinic.entity.Surgery;
import com.clinic.pet.petclinic.exceptions.ApplicationIllegalArgumentEx;
import com.clinic.pet.petclinic.exceptions.IllegalVisitStateException;
import com.clinic.pet.petclinic.exceptions.VisitNotFoundException;
import com.clinic.pet.petclinic.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class VisitService {
    private final VisitRepository visitRepository;
    private final AnimalRepository animalRepository;
    private final CustomerRepository customerRepository;
    private final SurgeryRepository surgeryRepository;
    private final VetRepository vetRepository;
    private final VisitMapper visitMapper;
    private final Clock clock;

    @Transactional(readOnly = true)
    public List<VisitResponseDto> getAllVisits() {
        log.info("Getting all Visits");
        var visits = visitRepository.findAll();
        return visitMapper.mapListToDto(visits);
    }

    @Transactional(readOnly = true)
    public Optional<VisitResponseDto> getVisitById(int id) {
        log.info("Getting Visit by id: {}", id);
        return visitRepository.findById(id)
                .map(visitMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<VisitResponseDto> getCustomersVisits(int id) {
        log.info("Getting customer's visits");
        var visits = visitRepository.getAllByCustomerId(id);
        return visitMapper.mapListToDto(visits);
    }

    @Transactional
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
        if (!checkIfVisitIsInAvailableTime(requestDto)) {
            log.error("Vet is not available in this time");
            throw new IllegalVisitStateException("The visit should be planned in Vet available time");
        }
        var customer = customerRepository.findById(requestDto.getCustomerID())
                .orElseThrow(() -> new ApplicationIllegalArgumentEx("Customer does not exist"));
        var animal = animalRepository.findById(requestDto.getAnimalId())
                .orElseThrow(() -> new ApplicationIllegalArgumentEx("Animal does not exist"));
        var vet = vetRepository.findById(requestDto.getVetId())
                .orElseThrow(() -> new ApplicationIllegalArgumentEx("Vet does not exist"));
        var surgery = chooseSurgery(requestDto);
        var visit = visitMapper.mapToEntity(requestDto, animal, vet, customer, surgery);
        var createdVisit = visitRepository.save(visit);
        log.info("Visit created id: {}", createdVisit.getId());
        return visitMapper.mapToDto(createdVisit);
    }

    @Transactional
    public void delete(int id) {
        if (!visitRepository.existsById(id)) {
            log.error("Visit is not found: {}", id);
            throw new VisitNotFoundException("Visit not found: " + id);
        }
        log.info("Deleting visit: {}", id);
        visitRepository.deleteById(id);
    }

    @Transactional
    public VisitResponseDto changeDescription(int id, VisitSetDescriptionRequestDto requestDto) {
        log.info("Changing visit (id: {}) description", id);
        var visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id));
        if ((visit.getStatus() != Status.FINISHED) && (visit.getStatus() != Status.NOT_APPEARED)) {
            throw new IllegalVisitStateException("Visit state restricts changing description");
        }
        visit.setDescription(requestDto.getDescription());
        var created = visitRepository.save(visit);
        log.info("Visit id: {} description was changed to: {}", id, requestDto.getDescription());
        return visitMapper.mapToDto(created);
    }

    @Transactional
    public VisitResponseDto changeVisitStatus(int id, VisitSetStatusRequestDto requestDto) {
        log.info("Changing visit status");
        var visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id));
        var status = visitMapper.mapStringToStatus(requestDto.getStatus());
        if ((status != Status.FINISHED) && (status != Status.NOT_APPEARED)) {
            throw new IllegalVisitStateException("Visit state restricts changing status");
        }
        visit.setStatus(status);
        var created = visitRepository.save(visit);
        log.info("Visit id: {} status was changed to: {}", id, requestDto.getStatus());
        return visitMapper.mapToDto(created);
    }

    @Transactional
    public List<VisitResponseDto> allVisitsWithVet(int id) {
        log.info("Getting all vet's id: {} visits", id);
        var visits = visitRepository.vetsVisits(id);
        return visitMapper.mapListToDto(visits);
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

    private boolean checkIfVisitIsInAvailableTime(VisitRequestDto requestDto) {
        var vet = vetRepository.findById(requestDto.getVetId())
                .orElseThrow(() -> new ApplicationIllegalArgumentEx("Vet does not exist"));
        var visitStartTime = requestDto.getStartTime().toLocalTime();
        var visitEndTime = requestDto.getStartTime().plus(requestDto.getDuration()).toLocalTime();
        return vet.getAvailabilityFrom().isBefore(visitStartTime) && vet.getAvailabilityTo().isAfter(visitEndTime);
    }

    private Surgery chooseSurgery(VisitRequestDto requestDto) {
        var visits = visitRepository.existVisitBetweenTime(requestDto.getVetId(), requestDto.getStartTime(),
                requestDto.getStartTime().plus(requestDto.getDuration()));
        var notAvailableSurgeriesIdList = visits.stream()
                .map(visit -> visit.getSurgery().getId())
                .collect(Collectors.toSet());
        return surgeryRepository.findAll().stream()
                .filter(surgery -> !notAvailableSurgeriesIdList.contains(surgery.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("No surgery available");
                    return new IllegalVisitStateException("No surgery available");
                });
    }
}