package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.exceptions.AnimalNotFoundException;
import com.clinic.pet.petclinic.repository.AnimalRepository;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class LoggedAnimalService implements AnimalService {
    private final CustomerRepository customerRepository;
    private final AnimalRepository animalRepository;
    private final AnimalMapper mapper;

    @Override
    public List<AnimalResponseDto> getAllAnimals() {
        log.info("Getting all animals");
        var animals = animalRepository.findAll();
        return mapper.mapListToDto(animals);
    }

    @Override
    public Optional<AnimalResponseDto> getAnimalById(int id) {
        log.info("Getting animal by id: {}", id);
        return animalRepository.findById(id)
                .map(mapper::mapToDto);
    }

    @Override
    public AnimalResponseDto addAnimal(AnimalRequestDto requestDto) {
        log.info("adding a new animal");
        var owner = customerRepository.findById(requestDto.getOwnerID())
                .orElseThrow(() -> new IllegalArgumentException("Owner does not exist"));
        Animal animal = mapper.mapToEntity(requestDto, owner);
        log.info("created new animal id: {}", animal.getId());
        var createdAnimal = animalRepository.save(animal);
        return mapper.mapToDto(createdAnimal);
    }

    @Override
    public void delete(int id) {
        if (!animalRepository.existsById(id)) {
            log.error("Animal is not found: {}", id);
            throw new AnimalNotFoundException("Animal not found: " + id);
        }
        animalRepository.deleteById(id);
    }
}
