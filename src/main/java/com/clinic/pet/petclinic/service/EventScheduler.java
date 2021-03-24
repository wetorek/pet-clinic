package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Profile("prod")
public class EventScheduler {
    private final VisitRepository visitRepository;

    @Scheduled(fixedRate = 3_600_000)
    public void endingVisits(){
        log.info("ending visits");
        var localDateTime = LocalDateTime.now();
        visitRepository.automaticEndingVisits(localDateTime);
    }
}
