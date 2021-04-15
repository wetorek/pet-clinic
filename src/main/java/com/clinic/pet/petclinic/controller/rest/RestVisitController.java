package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.*;
import com.clinic.pet.petclinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/visits")
@AllArgsConstructor
class RestVisitController {
    private final VisitService visitService;

    @GetMapping(produces = "application/hal+json")
    public List<VisitResponseDto> getAllVisits() {
        var visits = visitService.getAllVisits();
        return visits
                .stream()
                .map(this::represent)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}", produces = "application/hal+json")
    public ResponseEntity<VisitResponseDto> getVisit(@PathVariable @Min(1) int id) {
        var visit = visitService.getVisitById(id);
        var result = visit.map(this::represent).orElse(null);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/find", produces = "application/hal+json")
    public List<FreeSlotVisitResponseDto> findSlotForVisit(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
            ){
        var freeSlots = visitService.findFreeSlots(start, end);
        return freeSlots
                .stream()
                .map(this::represent)
                .collect(Collectors.toList());
    }

    @PostMapping(produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    VisitResponseDto createVisit(@Valid @RequestBody VisitRequestDto visitRequestDto) {
         var visit = visitService.createVisit(visitRequestDto);
         return represent(visit);
    }

    @PatchMapping(path = "/{id}/status", produces = "application/hal+json")
    public VisitResponseDto changeVisitStatus(@PathVariable @Min(1) int id, @RequestBody VisitSetStatusRequestDto requestDto) {
        var visit = visitService.changeVisitStatus(id, requestDto);
        return represent(visit);
    }

    @PatchMapping(path = "/{id}/description", produces = "application/hal+json")
    public VisitResponseDto changeDescriptionVisit(@PathVariable @Min(1) int id, @RequestBody VisitSetDescriptionRequestDto requestDto) {
        var visit = visitService.changeDescription(id, requestDto);
        return represent(visit);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable @Min(1) int id) {
        visitService.delete(id);
    }

    private VisitResponseDto represent(VisitResponseDto visit) {
        Link selfLink = linkTo(methodOn(RestVisitController.class).getVisit(visit.getId())).withSelfRel();
        Link allVisits = linkTo(methodOn(RestVisitController.class).getAllVisits()).withSelfRel();
        var representation = new VisitResponseDto(visit.getId(), visit.getStartTime(), visit.getDuration(),
                visit.getAnimal(), visit.getStatus(), visit.getPrice(), visit.getDescription());
        representation.add(selfLink, allVisits);
        return representation;
    }

    private FreeSlotVisitResponseDto represent(FreeSlotVisitResponseDto freeSlot) {
        Link allVisits = linkTo(methodOn(RestVisitController.class).getAllVisits()).withSelfRel();
        freeSlot.add(allVisits);
        return freeSlot;
    }
}
