package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.FreeSlotVisitResponseDto;
import com.clinic.pet.petclinic.entity.Vet;
import com.clinic.pet.petclinic.repository.VetRepository;
import com.clinic.pet.petclinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class FindVisitService {
    private final VetRepository vetRepository;
    private final VisitRepository visitRepository;
    private final VetMapper vetMapper;

    public List<FreeSlotVisitResponseDto> findFreeSlots(LocalDateTime start, LocalDateTime end) {
        log.info("Finding free slots between: {} and {}", start, end);
        return vetRepository.findAll().stream()
                .map(vet -> findFreeSlotsForVet(vet, start, end))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<FreeSlotVisitResponseDto> findFreeSlotsForVet(Vet vet, LocalDateTime start, LocalDateTime end) {
        List<FreeSlotVisitResponseDto> result = new ArrayList<>();
        LocalDateTime slotTime = start;
        while (slotTime.isBefore(end)) {
            log.info("Checking available visit between: {} and {}", slotTime, slotTime.plusMinutes(15));
            if (visitRepository.existVisitBetweenTime(vet.getId(), slotTime, slotTime.plusMinutes(15)).isEmpty() &&
                    checkIfVetIsAvailable(vet, slotTime, slotTime.plusMinutes(15))) {
                result.add(new FreeSlotVisitResponseDto(slotTime, vetMapper.mapToDto(vet)));
            }
            slotTime = slotTime.plusMinutes(15);
        }
        return result;
    }

    private boolean checkIfVetIsAvailable(Vet vet, LocalDateTime start, LocalDateTime end) {
        return vet.getAvailabilityFrom().isBefore(start.toLocalTime()) && vet.getAvailabilityTo().isAfter(end.toLocalTime());
    }

}
