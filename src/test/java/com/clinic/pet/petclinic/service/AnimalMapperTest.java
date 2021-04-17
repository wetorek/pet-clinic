package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalRequestDto;
import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.entity.Animal;
import com.clinic.pet.petclinic.entity.AnimalSpecies;
import com.clinic.pet.petclinic.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AnimalMapperTest {
    @Autowired
    private AnimalMapper animalMapper;

    @Test
    void mapEntityToDto() {
        var owner = new Customer(1, "John", "Doe");
        var input = new Animal(1, "Fluffy", LocalDate.of(2010, 10, 4), AnimalSpecies.DOG, owner);
        var expected = new AnimalResponseDto(1, "Fluffy", LocalDate.of(2010, 10, 4), "DOG", 1);

        var actual = animalMapper.mapToDto(input);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapListToDto() {
        var owner = new Customer(1, "John", "Doe");
        var input = List.of(new Animal(1, "Fluffy", LocalDate.of(2010, 10, 4), AnimalSpecies.DOG, owner));
        var expected = List.of(new AnimalResponseDto(1, "Fluffy", LocalDate.of(2010, 10, 4), "DOG", 1));

        var actual = animalMapper.mapListToDto(input);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void mapCreateDtoToEntity() {
        var owner = new Customer(1, "John", "Doe");
        var input = new AnimalRequestDto("Fluffy", LocalDate.of(2010, 10, 4), "DOG", 1);
        var expected = new Animal(null, "Fluffy", LocalDate.of(2010, 10, 4), AnimalSpecies.DOG, owner);

        var actual = animalMapper.mapToEntity(input, owner);

        assertThat(actual).isEqualTo(expected);
    }

}