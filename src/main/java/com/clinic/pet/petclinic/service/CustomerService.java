package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Role;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers() {
        log.info("Getting all customers");
        var customers = customerRepository.findAll();
        return mapper.mapListToDto(customers);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(int id) {
        log.info("Getting customer by id: {}", id);
        return customerRepository.findById(id)
                .map(mapper::mapToDto);
    }

    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto requestDto) {
        log.info("Creating a customer");
        var encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        var customer = mapper.mapToEntity(requestDto, Role.ROLE_CLIENT, AccountState.ACTIVE, encodedPassword);
        var createdCustomer = customerRepository.save(customer);
        log.info("Customer created id: {}", createdCustomer.getId());
        return mapper.mapToDto(createdCustomer);
    }

}
