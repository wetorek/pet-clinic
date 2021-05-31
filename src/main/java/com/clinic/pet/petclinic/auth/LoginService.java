package com.clinic.pet.petclinic.auth;

import com.clinic.pet.petclinic.controller.dto.AuthenticationRequestDto;
import com.clinic.pet.petclinic.controller.dto.AuthenticationResponseDto;
import com.clinic.pet.petclinic.entity.User;
import com.clinic.pet.petclinic.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationResponseDto login(AuthenticationRequestDto requestDto) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword()
                )
        );
        var user = (AuthenticatedUser) auth.getPrincipal();
        var role = userService.findUserByUsername(user.getUsername())
                .map(User::getRole)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + user.getUsername() + " not found"));
        var token = tokenService.generateNewToken(user, Collections.singletonList(role));
        return new AuthenticationResponseDto(token);
    }
}
