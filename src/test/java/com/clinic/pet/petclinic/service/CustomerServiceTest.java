package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Customer;
import com.clinic.pet.petclinic.entity.Role;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers() {
        var customerEntities = createCustomers();
        var expectedCustomerDtos = createCustomerResponses();
        when(customerRepository.findAll()).thenReturn(customerEntities);
        when(customerMapper.mapListToDto(any())).thenReturn(expectedCustomerDtos);

        var actual = customerService.getAllCustomers();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expectedCustomerDtos);
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).mapListToDto(customerEntities);
    }

    @Test
    void getExistingCustomerById() {
        var customerEntity = createCustomers().get(0);
        var expected = createCustomerResponses().get(0);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customerEntity));
        when(customerMapper.mapToDto(any())).thenReturn(expected);

        var actual = customerService.getCustomerById(1);

        assertThat(actual).contains(expected);
        verify(customerRepository, times(1)).findById(1);
        verify(customerMapper, times(1)).mapToDto(customerEntity);
    }

    @Test
    void getNonExistingCustomerById() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());

        var actual = customerService.getCustomerById(1);

        assertThat(actual).isEmpty();
        verify(customerRepository, only()).findById(1);
    }

    @Test
    void createCustomer() {
        var customerRequest = new CustomerRequestDto("Walt", "White", "ww123", "pass123");
        var customer = new Customer(1, "Walt", "White");
        var expectedResponse = new CustomerResponseDto(1, "Walt", "White", "ww123");
        when(customerMapper.mapToEntity(any(), any(), any(), any())).thenReturn(customer);
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerMapper.mapToDto(any())).thenReturn(expectedResponse);
        when(passwordEncoder.encode(any())).thenReturn("generatedPass");

        var actual = customerService.createCustomer(customerRequest);

        assertThat(actual).isEqualTo(expectedResponse);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).mapToDto(customer);
        verify(customerMapper, times(1))
                .mapToEntity(customerRequest, Role.ROLE_CLIENT, AccountState.ACTIVE, "generatedPass");
    }

    private List<Customer> createCustomers() {
        return List.of(
                new Customer(1, "John", "Doe"),
                new Customer(2, "Walter", "White")
        );
    }

    private List<CustomerResponseDto> createCustomerResponses() {
        return List.of(
                new CustomerResponseDto(1, "John", "Doe", "jd123"),
                new CustomerResponseDto(2, "Walter", "White", "ww1234")
        );
    }
}