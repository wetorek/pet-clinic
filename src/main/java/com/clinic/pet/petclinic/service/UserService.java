package com.clinic.pet.petclinic.service;

import com.clinic.pet.petclinic.controller.dto.UserRequestDto;
import com.clinic.pet.petclinic.controller.dto.UserResponseDto;
import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.User;
import com.clinic.pet.petclinic.exceptions.ResourceNotFoundException;
import com.clinic.pet.petclinic.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        log.info("Getting all users");
        var users = userRepository.findAll();
        return mapper.mapListToDto(users);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(int id) {
        log.info("Getting User by id: {}", id);
        return userRepository.findById(id)
                .map(mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new IllegalArgumentException("This credentials already exist");
        }
        var encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        var user = mapper.mapToEntity(userRequestDto, encodedPassword, AccountState.ACTIVE);
        userRepository.save(user);
        return mapper.mapToDto(user);
    }

    @Transactional
    public void activateUser(int id) {
        log.info("Updating user {} status on activated", id);
        var user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        user.setAccountState(AccountState.ACTIVE);
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(int id) {
        log.info("Updating user {} status on deactivated", id);
        var user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        user.setAccountState(AccountState.BLOCKED);
        userRepository.save(user);
    }
}
