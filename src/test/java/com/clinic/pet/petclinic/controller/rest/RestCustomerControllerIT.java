package com.clinic.pet.petclinic.controller.rest;

import com.clinic.pet.petclinic.controller.dto.CustomerRequestDto;
import com.clinic.pet.petclinic.controller.dto.CustomerResponseDto;
import com.clinic.pet.petclinic.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.endsWithIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RestCustomerControllerIT {
    private final String PATH = "/api/v1/customers";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerService customerService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(
                new CustomerResponseDto(1, "Walt", "White", "walt123")
        ));

        mockMvc
                .perform(get(PATH).accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("_embedded.customerResponseDtoList[0].id", is(1)))
                .andExpect(jsonPath("_embedded.customerResponseDtoList[0].name", is("Walt")))
                .andExpect(jsonPath("_embedded.customerResponseDtoList[0].surname", is("White")))
                .andExpect(jsonPath("_links.self.href", is(endsWithIgnoringCase("/api/v1/customers"))));
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCustomerById() throws Exception {
        when(customerService.getCustomerById(1)).thenReturn(Optional.of(
                new CustomerResponseDto(1, "Walt", "White", "walt123")
        ));

        mockMvc
                .perform(get(PATH + "/1").accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Walt")))
                .andExpect(jsonPath("$.surname", is("White")))
                .andExpect(jsonPath("$._links.self.href", is(endsWithIgnoringCase("/api/v1/customers/1"))));
        verify(customerService, times(1)).getCustomerById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    void createCustomer() throws Exception {
        var customerRequest = new CustomerRequestDto("Walt", "White", "ww1234", "dsadase123");
        var expected = new CustomerResponseDto(1, "Walt", "White", "ww1234");
        when(customerService.createCustomer(any())).thenReturn(expected);

        mockMvc
                .perform(
                        post(PATH)
                                .contentType("application/json")
                                .accept("application/hal+json")
                                .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Walt")))
                .andExpect(jsonPath("$.surname", is("White")))
                .andExpect(jsonPath("$._links.self.href", is(endsWithIgnoringCase("/api/v1/customers/1"))));
        verify(customerService, times(1)).createCustomer(customerRequest);
    }

}