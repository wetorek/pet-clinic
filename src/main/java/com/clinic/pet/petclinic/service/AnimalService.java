package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.exceptions.ApplicationIllegalArgumentEx;
import com.clinic.pet.petclinic.repository.AnimalRepository;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class AnimalService {
    private final CustomerRepository customerRepository;
    private final AnimalRepository animalRepository;
    private final AnimalMapper mapper;

    @Transactional(readOnly = true)
    public List<AnimalResponseDto> getAllAnimals() {
        log.info("Getting all animals");
        var animals = animalRepository.findAll();
        return mapper.mapListToDto(animals);
    }

    @Transactional(readOnly = true)
    public Optional<AnimalResponseDto> getAnimalById(int id) {
        log.info("Getting animal by id: {}", id);
        return animalRepository.findById(id)
                .map(mapper::mapToDto);
    }

    @Transactional
    public AnimalResponseDto createAnimal(AnimalRequestDto requestDto) {
        log.info("adding a new animal");
        var owner = customerRepository.findById(requestDto.getOwnerID())
                .orElseThrow(() -> new ApplicationIllegalArgumentEx("Owner does not exist"));
        Animal animal = mapper.mapToEntity(requestDto, owner);
        var createdAnimal = animalRepository.save(animal);
        log.info("created new animal id: {}", createdAnimal.getId());
        return mapper.mapToDto(createdAnimal);
    }

    @Transactional(readOnly = true)
    public List<AnimalResponseDto> getAnimalsByCustomerId(int id) {
        log.info("Getting customer's animals");
        var animals = animalRepository.getAllByOwnerId(id);
        return mapper.mapListToDto(animals);
    }
}
