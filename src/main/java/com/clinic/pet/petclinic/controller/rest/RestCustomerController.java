package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.AnimalResponseDto;
import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.controller.dto.VisitResponseDto;
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
        return representCollectionCustomers(customerResponseDtos);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VET')")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable @Min(1) int id) {
        return customerService.getCustomerById(id)
                .map(this::represent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/{id}/animals")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CUSTOMER') AND #id == authentication.principal.userId)")
    public CollectionModel<AnimalResponseDto> getCustomersAnimals(@PathVariable @Min(1) int id) {
        var result = customerService.customersAnimals(id)
                .stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollectionAnimals(result);
    }

    @GetMapping(path = "/{id}/visits")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CUSTOMER') AND #id == authentication.principal.userId)")
    public CollectionModel<VisitResponseDto> getCustomersVisits(@PathVariable @Min(1) int id){
        var result = customerService.getCustomersVisits(id)
                .stream()
                .map(this::represent)
                .collect(Collectors.toList());
        return representCollectionVisits(result);
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
    private AnimalResponseDto represent(AnimalResponseDto animal) {
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAnimal(animal.getId())).withSelfRel();
        var allVets = linkTo(methodOn(RestAnimalController.class).getAllAnimals()).withRel("allAnimals");
        return new AnimalResponseDto(animal.getId(), animal.getName(), animal.getDateOfBirth(),
                animal.getSpecies(), animal.getOwnerId()).add(selfLink, allVets);
    }
    private VisitResponseDto represent(VisitResponseDto responseDto) {
        var selfLink = linkTo(methodOn(RestVisitController.class).getVisit(responseDto.getId())).withSelfRel();
        return responseDto.add(selfLink);
    }

    private CollectionModel<VisitResponseDto> representCollectionVisits(Collection<VisitResponseDto> visitResponseDtos) {
        var selfLink = linkTo(methodOn(RestVisitController.class).getAllVisits()).withSelfRel();
        return CollectionModel.of(visitResponseDtos, selfLink);
    }

    private CollectionModel<CustomerResponseDto> representCollectionCustomers(Collection<CustomerResponseDto> customerResponseDtos) {
        var selfLink = linkTo(methodOn(RestCustomerController.class).getAllCustomers()).withSelfRel();
        return CollectionModel.of(customerResponseDtos, selfLink);
    }

    private CollectionModel<AnimalResponseDto> representCollectionAnimals(Collection<AnimalResponseDto> animalResponseDtos) {
        var selfLink = linkTo(methodOn(RestAnimalController.class).getAllAnimals()).withSelfRel();
        return CollectionModel.of(animalResponseDtos, selfLink);
    }
}
