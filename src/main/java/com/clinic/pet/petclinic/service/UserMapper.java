package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.UserRequestDto;
import com.clinic.pet.petclinic.controller.dto.UserResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Role;
import com.clinic.pet.petclinic.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto mapToDto(User user);

    List<UserResponseDto> mapListToDto(Collection<User> visits);

    @Mappings({
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "accountState", target = "accountState"),
            @Mapping(target = "id", ignore = true)
    })
    User mapToEntity(UserRequestDto userRequestDto, String password, AccountState accountState);

    default Role mapStringToRole(String role) {
        return Role.valueOf(role);
    }
}
