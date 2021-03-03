package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.Visit;
import com.clinic.pet.petclinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
@AllArgsConstructor
class VisitController {

    private final ModelMapper mapper;
    private final VisitService visitService;

//    @GetMapping
//    List<VisitResponseDto> getAll() {
//        List<VisitResponseDto> visitResponse = new ArrayList<>();
//        for (Visit v : visitService.getAllVisits()) {
//            visitResponse.add(mapToDto(v));
//        }
//        return visitResponse;
//    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getVisit(@PathVariable int id) {
        return visitService.getVisitById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Visit> getAllVisits() {
        return visitService.getAllVisits();
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<?> delete(@PathVariable int id) {
        if (visitService.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/{id}")
    ResponseEntity<?> createVisit(@PathVariable int id) {
        if (visitService.getVisitById(id).isPresent()){
            return ResponseEntity.badRequest().build();
        }

        //todo
        return ResponseEntity.ok().build();
    }

    private VisitResponseDto mapToDto(Visit visit) {
        return mapper.map(visit, VisitResponseDto.class);
    }

}
