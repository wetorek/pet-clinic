package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/customers", produces = "application/hal+json")
@AllArgsConstructor
public class RestCustomerController {
    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    public CollectionModel<CustomerResponseDto> getAllCustomers() {
        var customers = customerService.getAllCustomers();
        var customerResponseDtos = customers.stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollection(customerResponseDtos);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable @Min(1) int id) {
        return customerService.getCustomerById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    CustomerResponseDto createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        var customer = customerService.createCustomer(customerRequestDto);
        return represent(customer);
    }

    private CustomerResponseDto represent(CustomerResponseDto customer) {
        var selfLink = linkTo(methodOn(RestCustomerController.class).getCustomer(customer.getId())).withSelfRel();
        return new CustomerResponseDto(customer.getId(), customer.getName(), customer.getSurname(),
                customer.getUsername()).add(selfLink);
    }

    private CollectionModel<CustomerResponseDto> representCollection(Collection<CustomerResponseDto> customerResponseDtos) {
        var selfLink = linkTo(methodOn(RestCustomerController.class).getAllCustomers()).withSelfRel();
        return CollectionModel.of(customerResponseDtos, selfLink);
    }
}
