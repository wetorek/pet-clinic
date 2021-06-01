package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.UserResponseDto;
import com.clinic.pet.petclinic.controller.dto.VetResponseDto;
import com.clinic.pet.petclinic.entity.User;
import com.clinic.pet.petclinic.entity.Vet;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto mapToDto(User user);

    List<UserResponseDto> mapListToDto(Collection<User> visits);
}
