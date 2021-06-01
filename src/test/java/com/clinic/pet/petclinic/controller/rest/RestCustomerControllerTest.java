package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.service.CustomerService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
class RestCustomerControllerTest {
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private RestCustomerController customerController;

    @Test
    void getAllCustomers() {
        var expected = List.of(new CustomerResponseDto(1, "Walt", "White", "walt123"));
        Link selfLink = linkTo(methodOn(RestCustomerController.class).getCustomer(1)).withSelfRel();
        when(customerService.getAllCustomers()).thenReturn(expected);

        var actual = customerController.getAllCustomers();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        assertThat(actual.get(0))
                .extracting(u -> u.getLinks().toList(), InstanceOfAssertFactories.LIST)
                .containsExactlyInAnyOrder(selfLink);
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerById() {
        var expected = new CustomerResponseDto(1, "Walt", "White", "walt123");
        var selfLink = linkTo(methodOn(RestCustomerController.class).getCustomer(1)).withSelfRel();
        when(customerService.getCustomerById(1)).thenReturn(Optional.of(expected));

        var actual = customerController.getCustomer(1);

        assertThat(actual).extracting(ResponseEntity::getBody).isEqualTo(expected);
        assertThat(actual.getBody())
                .extracting(u -> u.getLinks().toList(), InstanceOfAssertFactories.LIST)
                .containsExactlyInAnyOrder(selfLink);
        verify(customerService, times(1)).getCustomerById(1);
    }

    @Test
    void getNotExistingCustomerById() {
        when(customerService.getCustomerById(1)).thenReturn(Optional.empty());

        var actual = customerController.getCustomer(1);

        assertThat(actual).extracting(ResponseEntity::getStatusCodeValue).isEqualTo(404);
    }

    @Test
    void createCustomer() {
        var customerRequest = new CustomerRequestDto("Walt", "White", "ww", "qwerty");
        var expected = new CustomerResponseDto(1, "Walt", "White", "ww");
        var selfLink = linkTo(methodOn(RestCustomerController.class).getCustomer(1)).withSelfRel();
        when(customerService.createCustomer(any())).thenReturn(expected);

        var actual = customerController.createCustomer(customerRequest);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getLinks().toList()).containsExactlyInAnyOrder(selfLink);
        verify(customerService, times(1)).createCustomer(customerRequest);
    }

}