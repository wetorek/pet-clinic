package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetDescriptionRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetStatusRequestDto;
import com.clinic.pet.petclinic.entity.Status;
import com.clinic.pet.petclinic.exceptions.VisitNotFoundException;
import com.clinic.pet.petclinic.repository.AnimalRepository;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import com.clinic.pet.petclinic.repository.VetRepository;
import com.clinic.pet.petclinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class LoggedVisitService implements VisitService {
    private final VisitRepository visitRepository;
    private final AnimalRepository animalRepository;
    private final CustomerRepository customerRepository;
    private final VetRepository vetRepository;
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

    private boolean checkDateTime(VisitRequestDto requestDto){
        Clock clock = Clock.systemUTC();
        LocalDateTime nowDateTime = LocalDateTime.now(clock);

        return !requestDto.getStartTime().isBefore(nowDateTime) &&
                !requestDto.getStartTime().isBefore(nowDateTime.plus(Duration.ofHours(1)));
    }

    public VisitResponseDto createVisit(VisitRequestDto requestDto) {
        log.info("Creating a Visit");
        if (checkIfVisitOverlaps(requestDto)) {
            log.error("Visit is overlapping");
            throw new IllegalStateException("This visit is overlapping");
        }
        if (!checkDateTime(requestDto)) {
            log.error("Visit cannot be planned on " + requestDto.getStartTime());
            throw new IllegalStateException("The visit cannot be scheduled in the past or for less than an hour");
        }
        var customer = customerRepository.findById(requestDto.getCustomerID())
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist"));
        var animal = animalRepository.findById(requestDto.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal does not exist"));
        var vet = vetRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Vet does not exist"));
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
        visit.setDescription(requestDto.getDescription());
        var created = visitRepository.save(visit);
        return mapper.mapToDto(created);
    }

    public VisitResponseDto changeVisitStatus(int id, VisitSetStatusRequestDto requestDto) {
        var visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id));
        var status = mapper.mapStringToStatus(requestDto.getStatus());
        if ((status != Status.FINISHED) && (status != Status.NOT_APPEARED)) {
            throw new IllegalStateException("Visit state restricts changing description");
        }
        visit.setStatus(status);
        var created = visitRepository.save(visit);
        return mapper.mapToDto(created);
    }

    private boolean checkIfVisitOverlaps(VisitRequestDto requestDto) {
        LocalDateTime endTime = requestDto.getStartTime().plus(requestDto.getDuration());
        return !visitRepository.existOverlapping(requestDto.getStartTime(), endTime).isEmpty();
    }
}