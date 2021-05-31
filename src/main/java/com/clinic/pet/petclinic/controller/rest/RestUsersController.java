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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/users", produces = "application/hal+json")
@AllArgsConstructor
public class RestUsersController {
    private final VetService vetService;
    private final CustomerService customerService;
    private final UserService userService;

    @PostMapping("/vets")
    @ResponseStatus(HttpStatus.CREATED)
    VetResponseDto createVet(@RequestBody VetRequestDto vetRequestDto) {
        var vet = vetService.createVet(vetRequestDto);
        return represent(vet);
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    CustomerResponseDto createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        var customer = customerService.createCustomer(customerRequestDto);
        return represent(customer);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        var user = userService.createUser(userRequestDto);
        return represent(user);
    }

    @GetMapping
    List<UserResponseDto> getAllUsers() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable @Min(1) int id) {
        return null;
    }

    @PatchMapping("/activate/{id}")
    public void activateUser(@PathVariable @Min(1) int id) {

    }

    @PatchMapping("/deactivate/{id}")
    public void deactivateUser(@PathVariable @Min(1) int id) {

    }

    private VetResponseDto represent(VetResponseDto responseDto) { //todo extract to controller.rest.utils
        var selfLink = linkTo(methodOn(RestVetController.class).getVet(responseDto.getId())).withSelfRel();
        var allVets = linkTo(methodOn(RestVetController.class).getAllVets()).withRel("allVets");
        var allVisits = linkTo(methodOn(RestVisitController.class).getAllVisitsWithVet(responseDto.getId())).withRel("allVetsVisit");
        responseDto.add(selfLink, allVets, allVisits);
        return responseDto;
    }

    private CustomerResponseDto represent(CustomerResponseDto customer) {
        var selfLink = linkTo(methodOn(RestCustomerController.class).getCustomer(customer.getId())).withSelfRel();
        var allCustomers = linkTo(methodOn(RestCustomerController.class).getAllCustomers()).withRel("allCustomers");
        var representation = new CustomerResponseDto(customer.getId(), customer.getName(), customer.getSurname(), customer.getUsername());
        representation.add(selfLink, allCustomers);
        return representation;
    }

    private UserResponseDto represent(UserResponseDto customer) {
//        var selfLink = linkTo(methodOn(RestUsersController.class). (customer.getId())).withSelfRel();
//        var allCustomers = linkTo(methodOn(RestCustomerController.class).getAllCustomers()).withRel("allCustomers");
//        var representation = new CustomerResponseDto(customer.getId(), customer.getName(), customer.getSurname());
//        representation.add(selfLink, allCustomers);
        return null;
    }

}
