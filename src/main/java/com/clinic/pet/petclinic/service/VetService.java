package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VetRequestDto;
import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.repository.VetRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class VetService {
    private final VetRepository vetRepository;
    private final VetMapper mapper;

    public List<VetResponseDto> getAllVets() {
        log.info("Getting all Vets");
        var visits = vetRepository.findAll();
        return mapper.mapListToDto(visits);
    }

    public Optional<VetResponseDto> getVetById(int id) {
        log.info("Getting Vet by id: {}", id);
        return vetRepository.findById(id)
                .map(mapper::mapToDto);
    }

    public VetResponseDto createVet(VetRequestDto requestDto) {
        log.info("Creating a vet");
        var vet = mapper.mapToEntity(requestDto);
        var createdVet = vetRepository.save(vet);
        log.info("Vet created id: {}", createdVet.getId());
        return mapper.mapToDto(createdVet);
    }
}