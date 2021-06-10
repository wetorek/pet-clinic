package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.auth.TokenService;
import com.clinic.pet.petclinic.controller.dto.LogoutRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/logout")
@AllArgsConstructor
public class RestLogoutController {
    private final TokenService tokenService;

    @PostMapping
    public void logout(@RequestBody @Valid LogoutRequestDto logoutRequestDto) {
        tokenService.blackListToken(logoutRequestDto);
    }
}
