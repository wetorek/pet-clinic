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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<VisitResponseDto> getAllVisits() {
        var visits = visitService.getAllVisits();
        var visitReponseDtos = visits.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollectionGetAll(visitReponseDtos);
    }

    @GetMapping(path = "/{id}")
    @PostAuthorize("hasRole('ADMIN') OR " +
            "(hasRole('VET') AND ( returnObject.statusCode.value() == 404 OR authentication.principal.userId == returnObject.body.vetId)) OR " +
            "((hasRole('CLIENT') AND (returnObject.statusCode.value() == 404 OR authentication.principal.userId == returnObject.body.customerId)))")
    public ResponseEntity<VisitResponseDto> getVisit(@PathVariable @Min(1) int id) {
        return visitService.getVisitById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/vet/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('VET') AND (authentication.principal.userId == #id))")
    public CollectionModel<VisitResponseDto> getAllVisitsWithVet(@PathVariable @Min(1) int id) {
        var visits = visitService.allVisitsWithVet(id);
        var visitReponseDtos = visits.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollectionGetAllByVet(visitReponseDtos, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("(hasRole('VET') AND authentication.principal.userId == #visitRequestDto.vetId) OR" +
            "(hasRole('CLIENT') AND authentication.principal.userId == #visitRequestDto.customerID)")
    VisitResponseDto createVisit(@Valid @RequestBody VisitRequestDto visitRequestDto) {
        var visit = visitService.createVisit(visitRequestDto);
        return represent(visit);
    }

    @PatchMapping(path = "/{id}/status")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('VET')")
    public VisitResponseDto changeVisitStatus(@PathVariable @Min(1) int id, @RequestBody VisitSetStatusRequestDto requestDto) {
        var visit = visitService.changeVisitStatus(id, requestDto);
        return represent(visit);
    }

    @PatchMapping(path = "/{id}/description")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('VET')")
    public VisitResponseDto changeDescriptionVisit(@PathVariable @Min(1) int id, @RequestBody VisitSetDescriptionRequestDto requestDto) {
        var visit = visitService.changeDescription(id, requestDto);
        return represent(visit);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') OR hasRole('VET')")
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
