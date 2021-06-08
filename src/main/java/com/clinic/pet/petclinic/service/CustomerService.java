package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Role;
import com.clinic.pet.petclinic.repository.AnimalRepository;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import com.clinic.pet.petclinic.repository.VisitRepository;
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
    private final CustomerMapper customerMapper;
    private final AnimalMapper animalMapper;
    private final PasswordEncoder passwordEncoder;
    private final AnimalRepository animalRepository;
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers() {
        log.info("Getting all customers");
        var customers = customerRepository.findAll();
        return customerMapper.mapListToDto(customers);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(int id) {
        log.info("Getting customer by id: {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::mapToDto);
    }

    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto requestDto) {
        log.info("Creating a customer");
        var encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        var customer = customerMapper.mapToEntity(requestDto, Role.ROLE_CLIENT, AccountState.ACTIVE, encodedPassword);
        var createdCustomer = customerRepository.save(customer);
        log.info("Customer created id: {}", createdCustomer.getId());
        return customerMapper.mapToDto(createdCustomer);
    }

    public List<AnimalResponseDto> customersAnimals(int id) {
        log.info("Getting customer's animals");
        var animals = animalRepository.getAllByOwnerId(id);
        return animalMapper.mapListToDto(animals);
    }

    public List<VisitResponseDto> getCustomersVisits(int id){
        log.info("Getting customer's visits");
        var visits = visitRepository.getAllByCustomerId(id);
        return visitMapper.mapListToDto(visits);
    }

}
