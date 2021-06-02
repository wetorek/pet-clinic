package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Customer;
import com.clinic.pet.petclinic.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerMapperIT {
    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void mapToDto() {
        var input = new Customer(1, "Walt", "Kowalski", "Walt123");
        var expected = new CustomerResponseDto(1, "Walt", "Kowalski", "Walt123");

        var actual = customerMapper.mapToDto(input);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapListToDto() {
        var input = List.of(new Customer(1, "Walt", "Kowalski", "Walt123"));
        var expected = List.of(new CustomerResponseDto(1, "Walt", "Kowalski", "Walt123"));

        var actual = customerMapper.mapListToDto(input);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void mapToEntity() {
        var input = new CustomerRequestDto("Walt", "Kowalski", "Walter", "walt123");
        var expected = new Customer(null, "Walter", "walt123", Role.ROLE_CLIENT, AccountState.ACTIVE, "Walt", "Kowalski");

        var actual = customerMapper.mapToEntity(input, Role.ROLE_CLIENT, AccountState.ACTIVE, "walt123");

        assertThat(actual).isEqualTo(expected);
    }

}