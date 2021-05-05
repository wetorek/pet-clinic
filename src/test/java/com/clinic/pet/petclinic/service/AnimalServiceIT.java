package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.entity.AnimalSpecies;
import com.clinic.pet.petclinic.entity.Customer;
import com.clinic.pet.petclinic.repository.AnimalRepository;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AnimalServiceIT {
    private static final LocalDate LOCAL_DATE_1 = LocalDate.of(1999, 7, 12);
    private static final LocalDate LOCAL_DATE_2 = LocalDate.of(2012, 3, 5);

    @Autowired
    private AnimalService animalService;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void populateDatabase() {
        var owner = new Customer(1, "John", "Doe");
        var animals = List.of(
                new Animal(1, "animal1", LOCAL_DATE_1, AnimalSpecies.CAT, owner),
                new Animal(2, "animal2", LOCAL_DATE_2, AnimalSpecies.DOG, owner)
        );
        customerRepository.save(owner);
        animalRepository.saveAll(animals);
    }

    @Test
    void getAllAnimals() {
        var expected = createAnimalResponses();

        var actual = animalService.getAllAnimals();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void getExistingAnimalById() {
        var expected = createAnimalResponses().get(0);

        var actual = animalService.getAnimalById(1);

        assertThat(actual).contains(expected);
    }

    @Test
    void getNonExistingAnimalById() {
        var actual = animalService.getAnimalById(11);

        assertThat(actual).isEmpty();
    }

    @Test
    void createAnimal() {
        var input = new AnimalRequestDto("Doggo", LOCAL_DATE_1, "DOG", 1);
        var expected = new AnimalResponseDto(3, "Doggo", LOCAL_DATE_1, "DOG", 1);

        var actual = animalService.createAnimal(input);

        assertThat(actual).isEqualTo(expected);
    }

    private List<AnimalResponseDto> createAnimalResponses() {
        return List.of(
                new AnimalResponseDto(1, "animal1", LOCAL_DATE_1, "CAT", 1),
                new AnimalResponseDto(2, "animal2", LOCAL_DATE_2, "DOG", 1)
        );
    }
}
