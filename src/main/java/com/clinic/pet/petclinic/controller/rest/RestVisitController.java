package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetDescriptionRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetStatusRequestDto;
import com.clinic.pet.petclinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/visits", produces = "application/hal+json")
@AllArgsConstructor
class RestVisitController {
    private final VisitService visitService;

    @GetMapping
    public CollectionModel<VisitResponseDto> getAllVisits() {
        var visits = visitService.getAllVisits();
        var visitReponseDtos = visits.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollectionGetAll(visitReponseDtos);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<VisitResponseDto> getVisit(@PathVariable @Min(1) int id) {
        return visitService.getVisitById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/vet/{id}")
    public CollectionModel<VisitResponseDto> getAllVisitsWithVet(@PathVariable @Min(1) int id) {
        var visits = visitService.allVisitsWithVet(id);
        var visitReponseDtos = visits.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollectionGetAllByVet(visitReponseDtos, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    VisitResponseDto createVisit(@Valid @RequestBody VisitRequestDto visitRequestDto) {
        var visit = visitService.createVisit(visitRequestDto);
        return represent(visit);
    }

    @PatchMapping(path = "/{id}/status")
    public VisitResponseDto changeVisitStatus(@PathVariable @Min(1) int id, @RequestBody VisitSetStatusRequestDto requestDto) {
        var visit = visitService.changeVisitStatus(id, requestDto);
        return represent(visit);
    }

    @PatchMapping(path = "/{id}/description")
    public VisitResponseDto changeDescriptionVisit(@PathVariable @Min(1) int id, @RequestBody VisitSetDescriptionRequestDto requestDto) {
        var visit = visitService.changeDescription(id, requestDto);
        return represent(visit);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable @Min(1) int id) {
        visitService.delete(id);
    }

    private VisitResponseDto represent(VisitResponseDto responseDto) {
        var selfLink = linkTo(methodOn(RestVisitController.class).getVisit(responseDto.getId())).withSelfRel();
        return responseDto.add(selfLink);
    }

    private CollectionModel<VisitResponseDto> representCollectionGetAll(Collection<VisitResponseDto> visitResponseDtos) {
        var selfLink = linkTo(methodOn(RestVisitController.class).getAllVisits()).withSelfRel();
        return CollectionModel.of(visitResponseDtos, selfLink);
    }

    private CollectionModel<VisitResponseDto> representCollectionGetAllByVet(
            Collection<VisitResponseDto> visitResponseDtos, int vetId) {
        var selfLink = linkTo(methodOn(RestVisitController.class).getAllVisitsWithVet(vetId)).withSelfRel();
        var allLink = linkTo(methodOn(RestVisitController.class).getAllVisits()).withRel("allVisits");
        return CollectionModel.of(visitResponseDtos, selfLink, allLink);
    }
}
