package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.service.CustomerService;
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
@RequestMapping(value = "/api/v1/customers", produces = "application/hal+json")
@AllArgsConstructor
public class RestCustomerController {
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        var customers = customerService.getAllCustomers();
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CustomerResponseDto createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        var customer = customerService.createCustomer(customerRequestDto);
        return represent(customer);
    }

    private CustomerResponseDto represent(CustomerResponseDto customer) {
        var selfLink = linkTo(methodOn(RestCustomerController.class).getCustomer(customer.getId())).withSelfRel();
        return new CustomerResponseDto(customer.getId(), customer.getName(), customer.getSurname(),
                customer.getUsername()).add(selfLink);
    }
}
