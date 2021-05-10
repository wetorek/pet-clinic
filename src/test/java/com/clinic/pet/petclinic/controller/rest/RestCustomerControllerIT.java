package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RestCustomerController.class)
class RestCustomerControllerIT {
    private final String PATH = "/api/v1/customers";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerService customerService;

    @Test
    void getAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(
                new CustomerResponseDto(1, "Walt", "White")
        ));

        mockMvc
                .perform(get(PATH).accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Walt")))
                .andExpect(jsonPath("$[0].surname", is("White")))
                .andExpect(jsonPath("$[0].links[?(@.rel=='self')].href", hasItem(endsWithIgnoringCase("/api/v1/customers/1"))))
                .andExpect(jsonPath("$[0].links[?(@.rel=='allCustomers')].href", hasItem(endsWithIgnoringCase("/api/v1/customers"))));
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerById() throws Exception {
        when(customerService.getCustomerById(1)).thenReturn(Optional.of(
                new CustomerResponseDto(1, "Walt", "White")
        ));

        mockMvc
                .perform(get(PATH + "/1").accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Walt")))
                .andExpect(jsonPath("$.surname", is("White")))
                .andExpect(jsonPath("$._links.self.href", is(endsWithIgnoringCase("/api/v1/customers/1"))))
                .andExpect(jsonPath("$._links.allCustomers.href", is(endsWithIgnoringCase("/api/v1/customers"))));
        verify(customerService, times(1)).getCustomerById(1);
    }

    @Test
    void getNotExistingCustomerById() throws Exception {
        when(customerService.getCustomerById(1)).thenReturn(Optional.empty());

        mockMvc
                .perform(get(PATH + "/1").accept("application/hal+json"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$").doesNotExist());
        verify(customerService, times(1)).getCustomerById(1);

    }

    @Test
    void createCustomer() throws Exception {
        var customerRequest = new CustomerRequestDto("Walt", "White");
        var expected = new CustomerResponseDto(1, "Walt", "White");
        when(customerService.createCustomer(any())).thenReturn(expected);

        mockMvc
                .perform(
                        post(PATH)
                                .contentType("application/json")
                                .accept("application/hal+json")
                                .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Walt")))
                .andExpect(jsonPath("$.surname", is("White")))
                .andExpect(jsonPath("$._links.self.href", is(endsWithIgnoringCase("/api/v1/customers/1"))))
                .andExpect(jsonPath("$._links.allCustomers.href", is(endsWithIgnoringCase("/api/v1/customers"))));
        verify(customerService, times(1)).createCustomer(customerRequest);
    }

}