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

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class RestCustomerController {
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable @Min(1) int id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CustomerResponseDto createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        return customerService.addCustomer(customerRequestDto);
    }
}
