package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.repository.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Profile("prod")
public class EventScheduler {
    private final VisitRepository visitRepository;
    private final Clock clock;

    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void endingVisits() {
        log.info("Ending visits");
        var localDateTime = LocalDateTime.now(clock);
        visitRepository.automaticEndingVisits(localDateTime);
    }
}
