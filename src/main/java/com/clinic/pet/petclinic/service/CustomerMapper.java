package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Customer;
import com.clinic.pet.petclinic.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponseDto mapToDto(Customer customer);

    List<CustomerResponseDto> mapListToDto(List<Customer> customers);

    @Mappings({
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "accountState", target = "accountState"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "animalList", ignore = true),
            @Mapping(target = "visitList", ignore = true)
    })
    Customer mapToEntity(CustomerRequestDto requestDto, Role role, AccountState accountState, String password);
}
