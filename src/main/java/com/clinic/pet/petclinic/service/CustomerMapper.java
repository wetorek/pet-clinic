package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponseDto mapToDto(Customer customer);

    List<CustomerResponseDto> mapListToDto(List<Customer> customers);
}
