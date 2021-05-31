package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.UserRequestDto;
import com.clinic.pet.petclinic.controller.dto.UserResponseDto;
import com.clinic.pet.petclinic.entity.User;
import com.clinic.pet.petclinic.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
//        if (userRepository.existsByUsername(userRegisterDto.getUsername())) {
//            throw new IllegalArgumentException("This credentials already exist");
//        }
//        var encodedPassword = passwordEncoder.encode(userRegisterDto.getPassword());
//        var user = new User(userRegisterDto.getUsername(), encodedPassword, userRegisterDto.getName(), userRegisterDto.getSurname(),
//                userRegisterDto.getEmail(), List.of(Role.ROLE_USER));
//        userRepository.save(user);
        return null;
    }
}
