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

    @GetMapping("/vet")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('VET') AND #vetId == authentication.principal.userId)")
    public CollectionModel<VisitResponseDto> getAllVisitsWithVet(@RequestParam @Min(1) int vetId) {
        var visits = visitService.allVisitsWithVet(vetId);
        var visitReponseDtos = visits.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollectionGetAllByVet(visitReponseDtos, vetId);
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENT') AND #customerId == authentication.principal.userId)")
    public CollectionModel<VisitResponseDto> getVisitsByCustomer(@RequestParam @Min(1) int customerId) {
        var result = visitService.getCustomersVisits(customerId)
                .stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollectionGetAll(result);
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
    @PreAuthorize("hasAnyRole('VET', 'ADMIN')")
    public VisitResponseDto changeVisitStatus(@PathVariable @Min(1) int id,
                                              @Valid @RequestBody VisitSetStatusRequestDto requestDto) {
        var visit = visitService.changeVisitStatus(id, requestDto);
        return represent(visit);
    }

    @PatchMapping(path = "/{id}/description")
    @PreAuthorize("hasAnyRole('VET', 'ADMIN')")
    public VisitResponseDto changeDescriptionVisit(@PathVariable @Min(1) int id,
                                                   @Valid @RequestBody VisitSetDescriptionRequestDto requestDto) {
        var visit = visitService.changeDescription(id, requestDto);
        return represent(visit);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('VET', 'ADMIN')")
    void delete(@PathVariable @Min(1) int id) {
        visitService.delete(id);
    }

    private VisitResponseDto represent(VisitResponseDto responseDto) {
        var selfLink = linkTo(methodOn(RestVisitController.class).getVisit(responseDto.getId())).withSelfRel();
        var vet = linkTo(methodOn(RestVetController.class).getVet(responseDto.getVetId())).withRel("vet");
        var customer = linkTo(methodOn(RestCustomerController.class).getCustomer(responseDto.getCustomerId())).withRel("owner");
        var animal = linkTo(methodOn(RestAnimalController.class).getAnimal(responseDto.getAnimalId())).withRel("animal");
        var allVisits = linkTo(methodOn(RestVisitController.class).getAllVisits()).withRel("all visits");
        return responseDto.add(selfLink, vet, customer, animal ,allVisits);
    }

    private CollectionModel<VisitResponseDto> representCollectionGetAll(Collection<VisitResponseDto> visitResponseDtos) {
        var selfLink = linkTo(methodOn(RestVisitController.class).getAllVisits()).withSelfRel();
        var vets = linkTo(methodOn(RestVetController.class).getAllVets()).withRel("vets");
        return CollectionModel.of(visitResponseDtos, selfLink, vets);
    }

    private CollectionModel<VisitResponseDto> representCollectionGetAllByVet(
            Collection<VisitResponseDto> visitResponseDtos, int vetId) {
        var selfLink = linkTo(methodOn(RestVisitController.class).getAllVisitsWithVet(vetId)).withSelfRel();
        var allLink = linkTo(methodOn(RestVisitController.class).getAllVisits()).withRel("allVisits");
        return CollectionModel.of(visitResponseDtos, selfLink, allLink);
    }
}
