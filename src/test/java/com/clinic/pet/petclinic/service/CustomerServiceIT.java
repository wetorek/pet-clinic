package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.entity.Customer;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerServiceIT {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void populateDb() {
        var customers = List.of(
                new Customer(1, "John", "Doe"),
                new Customer(2, "Walt", "Kowalski")
        );
        customerRepository.saveAll(customers);
    }

    @Test
    void getAllCustomers() {
        var expected = createCustomerResponses();

        var actual = customerService.getAllCustomers();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void getExistingCustomerById() {
        var expected = createCustomerResponses().get(0);

        var actual = customerService.getCustomerById(1);

        assertThat(actual).contains(expected);
    }

    @Test
    void getNonExistingCustomerById() {
        var actual = customerService.getCustomerById(3);

        assertThat(actual).isEmpty();
    }

    @Test
    void createCustomer() {
        var input = new CustomerRequestDto("Sławomir", "Kiełbasa");
        var expected = new CustomerResponseDto(3, "Sławomir", "Kiełbasa");

        var actual = customerService.createCustomer(input);

        assertThat(actual).isEqualTo(expected);
    }

    private List<CustomerResponseDto> createCustomerResponses() {
        return List.of(
                new CustomerResponseDto(1, "John", "Doe"),
                new CustomerResponseDto(2, "Walt", "Kowalski")
        );
    }
}