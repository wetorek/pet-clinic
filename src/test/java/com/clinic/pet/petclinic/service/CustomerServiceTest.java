package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.entity.Customer;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        var customerRequest = new CustomerRequestDto("Walt", "White");
        var customer = new Customer(1, "Walt", "White");
        var expectedResponse = new CustomerResponseDto(1, "Walt", "White");
        when(customerMapper.mapToEntity(any())).thenReturn(customer);
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerMapper.mapToDto(any())).thenReturn(expectedResponse);

        var actual = customerService.createCustomer(customerRequest);

        assertThat(actual).isEqualTo(expectedResponse);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).mapToDto(customer);
        verify(customerMapper, times(1)).mapToEntity(customerRequest);
    }

    private List<Customer> createCustomers() {
        return List.of(
                new Customer(1, "John", "Doe"),
                new Customer(2, "Walter", "White")
        );
    }

    private List<CustomerResponseDto> createCustomerResponses() {
        return List.of(
                new CustomerResponseDto(1, "John", "Doe"),
                new CustomerResponseDto(2, "Walter", "White")
        );
    }
}