package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerMapperTest {
    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void mapToDto() {
        var input = new Customer(1, "Walt", "Kowalski");
        var expected = new CustomerResponseDto(1, "Walt", "Kowalski");

        var actual = customerMapper.mapToDto(input);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapListToDto() {
        var input = List.of(new Customer(1, "Walt", "Kowalski"));
        var expected = List.of(new CustomerResponseDto(1, "Walt", "Kowalski"));

        var actual = customerMapper.mapListToDto(input);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void mapToEntity() {
        var input = new CustomerRequestDto("Walt", "Kowalski");
        var expected = new Customer(null, "Walt", "Kowalski");

        var actual = customerMapper.mapToEntity(input);

        assertThat(actual).isEqualTo(expected);
    }

}