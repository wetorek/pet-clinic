package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.entity.Vet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VetMapperTest {
    @Autowired
    private VetMapper vetMapper;

    @Test
    void mapEntityToDto() {
        var vet = new Vet(1, "Walt", "Kowalski", LocalTime.of(8, 0), LocalTime.of(16, 0), null);
        var expected = new VetResponseDto(1, "Walt", "Kowalski", LocalTime.of(8, 0), LocalTime.of(16, 0));

        var actual = vetMapper.mapToDto(vet);

        assertThat(actual).isEqualTo(expected);
    }
}