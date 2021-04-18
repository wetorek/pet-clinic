package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.FreeSlotVisitResponseDto;
import com.clinic.pet.petclinic.service.FindVisitService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/timeslots", produces = "application/hal+json")
@AllArgsConstructor
public class RestFindVisitController {
    private final FindVisitService findVisitService;

    @GetMapping(path = "/find")
    public List<FreeSlotVisitResponseDto> findSlotForVisit(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        var freeSlots = findVisitService.findFreeSlots(start, end);
        return freeSlots.stream()
                .map(this::represent)
                .collect(Collectors.toList());
    }

    private FreeSlotVisitResponseDto represent(FreeSlotVisitResponseDto freeSlot) {
        var allVisits = linkTo(methodOn(RestVisitController.class).getAllVisits()).withSelfRel();
        freeSlot.add(allVisits);
        return freeSlot;
    }
}
