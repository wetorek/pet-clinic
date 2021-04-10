package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetDescriptionRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitSetStatusRequestDto;
import com.clinic.pet.petclinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/v1/visits")
@AllArgsConstructor
class RestVisitController {
    private final VisitService visitService;

    @GetMapping
    public List<VisitResponseDto> getAllVisits() {
        return visitService.getAllVisits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponseDto> getVisit(@PathVariable @Min(1) int id) {
        return visitService.getVisitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    VisitResponseDto createVisit(@Valid @RequestBody VisitRequestDto visitRequestDto) {
        return visitService.createVisit(visitRequestDto);
    }

    @PatchMapping(path = "/{id}/status")
    public VisitResponseDto changeVisitStatus(@PathVariable @Min(1) int id, @RequestBody VisitSetStatusRequestDto requestDto) {
        return visitService.changeVisitStatus(id, requestDto);
    }

    @PatchMapping(path = "/{id}/description")
    public VisitResponseDto changeDescriptionVisit(@PathVariable @Min(1) int id, @RequestBody VisitSetDescriptionRequestDto requestDto) {
        return visitService.changeDescription(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable @Min(1) int id) {
        visitService.delete(id);
    }
}
