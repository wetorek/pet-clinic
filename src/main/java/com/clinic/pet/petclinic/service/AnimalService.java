package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;

import java.util.List;
import java.util.Optional;

public interface AnimalService {

    List<AnimalResponseDto> getAllAnimals();

    Optional<AnimalResponseDto> getAnimalById(int id);

    AnimalResponseDto addAnimal(AnimalRequestDto requestDto);
}
