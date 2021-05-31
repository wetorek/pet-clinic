package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.auth.LoginService;
import com.clinic.pet.petclinic.controller.dto.AuthenticationRequestDto;
import com.clinic.pet.petclinic.controller.dto.AuthenticationResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/authenticate")
@AllArgsConstructor
class RestAuthenticationController {
    private final LoginService loginService;

    @PostMapping
    AuthenticationResponseDto login(@Valid @RequestBody AuthenticationRequestDto authRequest) {
        return loginService.login(authRequest);
    }
}
