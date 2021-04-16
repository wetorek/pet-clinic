package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.entity.AnimalSpecies;
import com.clinic.pet.petclinic.entity.Customer;
import com.clinic.pet.petclinic.repository.AnimalRepository;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {
    private static final LocalDate LOCAL_DATE_1 = LocalDate.of(1999, 7, 12);
    private static final LocalDate LOCAL_DATE_2 = LocalDate.of(2012, 3, 5);

    @Mock
    private AnimalRepository animalRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AnimalMapper animalMapper;
    @InjectMocks
    private AnimalService animalService;

    @Test
    void returnAllAnimals() {
        var animals = createAnimals();
        when(animalRepository.findAll()).thenReturn(animals);
        var animalResponses = createAnimalResponses();
        when(animalMapper.mapListToDto(any())).thenReturn(animalResponses);

        var result = animalService.getAllAnimals();

        assertThat(result).containsExactlyInAnyOrderElementsOf(animalResponses);
        verify(animalRepository, times(1)).findAll();
        verify(animalMapper, times(1)).mapListToDto(animals);
    }

    @Test
    void returnExistingAnimalById() {
        var animal = createAnimals().get(0);
        var animalResponseDto = createAnimalResponses().get(0);
        when(animalRepository.findById(any())).thenReturn(Optional.of(animal));
        when(animalMapper.mapToDto(any())).thenReturn(animalResponseDto);

        var result = animalService.getAnimalById(1);

        assertThat(result).contains(animalResponseDto);
        verify(animalRepository, times(1)).findById(1);
        verify(animalMapper, times(1)).mapToDto(animal);
    }

    @Test
    void returnNotExistingAnimalById() {
        when(animalRepository.findById(any())).thenReturn(Optional.empty());

        var result = animalService.getAnimalById(1);

        assertThat(result).isEmpty();
        verify(animalRepository, Mockito.only()).findById(1);
    }

    @Test
    void createAnimal() {
        var requestDto = new AnimalRequestDto("animal1", LOCAL_DATE_1, "CAT", 1);
        var createdAnimal = createAnimals().get(0);
        var owner = createdAnimal.getOwner();
        var mappedAnimal = createAnimalResponses().get(0);
        when(customerRepository.findById(any())).thenReturn(Optional.of(owner));
        when(animalRepository.save(any())).thenReturn(createdAnimal);
        when(animalMapper.mapToDto(any())).thenReturn(mappedAnimal);

        var result = animalService.createAnimal(requestDto);

        assertThat(result).isEqualTo(mappedAnimal);
        verify(customerRepository, times(1)).findById(1);
        verify(animalRepository, times(1)).save(any());
        verify(animalMapper, times(1)).mapToDto(createdAnimal);
    }

    private List<Animal> createAnimals() {
        var owner = new Customer(1, "John", "Doe");
        return List.of(
                new Animal(1, "animal1", LOCAL_DATE_1, AnimalSpecies.CAT, owner),
                new Animal(2, "animal2", LOCAL_DATE_2, AnimalSpecies.DOG, owner)
        );
    }

    private List<AnimalResponseDto> createAnimalResponses() {
        return List.of(
                new AnimalResponseDto(1, "animal1", LOCAL_DATE_1, "CAT", 1),
                new AnimalResponseDto(2, "animal2", LOCAL_DATE_2, "DOG", 1)
        );
    }
}