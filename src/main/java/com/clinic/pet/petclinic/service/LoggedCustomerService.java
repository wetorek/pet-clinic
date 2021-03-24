package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.exceptions.CustomerNotFoundException;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class LoggedCustomerService implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        log.info("Getting all customers");
        var customers = customerRepository.findAll();
        return mapper.mapListToDto(customers);
    }

    @Override
    public Optional<CustomerResponseDto> getCustomerById(int id) {
        log.info("Getting customer by id: {}", id);
        return customerRepository.findById(id)
                .map(mapper::mapToDto);
    }

    @Override
    public CustomerResponseDto addCustomer(CustomerRequestDto requestDto) {
        log.info("Creating a customer");
        var customer = mapper.mapToEntity(requestDto);
        var createdCustomer = customerRepository.save(customer);
        log.info("Customer created id: ");
        return mapper.mapToDto(createdCustomer);
    }

    @Override
    public void delete(int id) {
        if (!customerRepository.existsById(id)) {
            log.error("Customer is not found: {}", id);
            throw new CustomerNotFoundException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }
}