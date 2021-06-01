package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.*;
import com.clinic.pet.petclinic.service.CustomerService;
import com.clinic.pet.petclinic.service.UserService;
import com.clinic.pet.petclinic.service.VetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/users", produces = "application/hal+json")
@AllArgsConstructor
public class RestUsersController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        var user = userService.createUser(userRequestDto);
        return represent(user);
    }

    @GetMapping
    List<UserResponseDto> getAllUsers() {
        var users = userService.getAllUsers();
        return users.stream()
                .map(this::represent)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable @Min(1) int id) {
        return userService.getUserById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/activate/{id}")
    public void activateUser(@PathVariable @Min(1) int id) {
        userService.activateUser(id);
    }

    @PatchMapping("/deactivate/{id}")
    public void deactivateUser(@PathVariable @Min(1) int id) {
        userService.deactivateUser(id);
    }

    private UserResponseDto represent(UserResponseDto user) {
        var selfLink = linkTo(methodOn(RestUsersController.class).getUser(user.getId())).withSelfRel();
        var allAdmins = linkTo(methodOn(RestUsersController.class).getAllUsers()).withRel("all admins");
        return new UserResponseDto(user.getId(), user.getUsername()).add(selfLink).add(allAdmins);
    }

}
