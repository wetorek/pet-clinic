package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
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
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class RestCustomerController {
    private final CustomerService customerService;

    @GetMapping(produces = "application/hal+json")
    public List<CustomerResponseDto> getAllCustomers() {
        var customers = customerService.getAllCustomers();
        return customers
                .stream()
                .map(this::represent)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}", produces = "application/hal+json")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable @Min(1) int id) {
        var customer = customerService.getCustomerById(id);
        var result = customer.map(this::represent).orElse(null);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    CustomerResponseDto createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        var customer = customerService.addCustomer(customerRequestDto);
        return represent(customer);
    }

    private CustomerResponseDto represent(CustomerResponseDto customer) {
        Link selfLink = linkTo(methodOn(RestCustomerController.class).getCustomer(customer.getId())).withSelfRel();
        Link allCustomers = linkTo(methodOn(RestCustomerController.class).getAllCustomers()).withSelfRel();
        var representation = new CustomerResponseDto(customer.getId(), customer.getName(), customer.getSurname());
        representation.add(selfLink, allCustomers);
        return representation;
    }
}
