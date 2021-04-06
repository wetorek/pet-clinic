package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerResponseDto> getAllCustomers();

    Optional<CustomerResponseDto> getCustomerById(int id);

    CustomerResponseDto addCustomer(CustomerRequestDto requestDto);
}
