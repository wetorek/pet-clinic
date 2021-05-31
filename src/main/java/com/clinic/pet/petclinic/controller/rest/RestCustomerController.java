package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/customers", produces = "application/hal+json")
@AllArgsConstructor
public class RestCustomerController {
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        var customers = customerService.getAllCustomers();
        //todo we do not create link to whole collection- apply to all controllers
        return customers.stream()
                .map(this::represent)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable @Min(1) int id) {
        return customerService.getCustomerById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private CustomerResponseDto represent(CustomerResponseDto customer) {
        var selfLink = linkTo(methodOn(RestCustomerController.class).getCustomer(customer.getId())).withSelfRel();
        var allCustomers = linkTo(methodOn(RestCustomerController.class).getAllCustomers()).withRel("allCustomers");
        //todo why?- apply to all controllers
        var representation = new CustomerResponseDto(customer.getId(), customer.getName(), customer.getSurname(), customer.getUsername());
        representation.add(selfLink, allCustomers);
        return representation;
    }
}
