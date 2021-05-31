package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.VisitRequestDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VisitMapperIT {
    private static final LocalDate LOCAL_DATE_1 = LocalDate.of(1999, 7, 12);
    private static final LocalDateTime LOCAL_DATE_TIME_1 = LocalDateTime.of(1999, 7, 12, 10, 0);

    @Autowired
    private VisitMapper visitMapper;

    @Test
    void mapVisitToDto() {
        var input = createVisit();
        var excepted = createVisitResponseDto();

        var actual = visitMapper.mapToDto(input);

        assertThat(actual).isEqualTo(excepted);
    }

    @Test
    void mapListToDto() {
        var input = List.of(createVisit());
        var excepted = List.of(createVisitResponseDto());

        var actual = visitMapper.mapListToDto(input);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(excepted);
    }

    @Test
    void mapToEntity() {
        var visitRequest = new VisitRequestDto(LOCAL_DATE_TIME_1, Duration.ofMinutes(20), 1, "PLANNED", BigDecimal.TEN, 1, "", 1);
        var expected = createVisit();
        expected.setId(null);

        var actual = visitMapper.mapToEntity(visitRequest, expected.getAnimal(), expected.getVet(), expected.getCustomer(), expected.getSurgery());

        assertThat(actual).isEqualTo(expected);
    }

    private Visit createVisit() {
        var owner = new Customer(1, "John", "Doe");
        var animal = new Animal(1, "animal1", LOCAL_DATE_1, AnimalSpecies.CAT, owner);
        var vet = new Vet(1, "Walt", "Kowalski", LocalTime.of(8, 0), LocalTime.of(16, 0));
        var surgery = new Surgery(1, "Surgery 1");
        return new Visit(1, LOCAL_DATE_TIME_1, Duration.ofMinutes(20), Status.PLANNED, BigDecimal.TEN, "", animal, owner, vet, surgery);
    }

    private VisitResponseDto createVisitResponseDto() {
        return new VisitResponseDto(1, LOCAL_DATE_TIME_1, Duration.ofMinutes(20), "PLANNED", BigDecimal.TEN,
                "", 1, 1, 1, 1);
    }

}