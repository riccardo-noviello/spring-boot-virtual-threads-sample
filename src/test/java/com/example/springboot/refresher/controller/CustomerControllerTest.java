package com.example.springboot.refresher.controller;

import com.example.springboot.refresher.persistence.model.Customer;
import com.example.springboot.refresher.persistence.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerRepository customerRepository;

    private CustomerController customerController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerController = new CustomerController(customerRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getAllCustomers() throws Exception {
        Customer customer = new Customer(UUID.randomUUID(), "John", "Doe");
        Mockito.when(customerRepository.findAll()).thenReturn(List.of(customer));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getCustomerByIdFound() throws Exception {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Jane", "Smith");
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer() throws Exception {
        Customer customer = new Customer(null, "Alice", "Wonderland");
        Customer savedCustomer = new Customer(UUID.randomUUID(), "Alice", "Wonderland");

        Mockito.when(customerRepository.save(savedCustomer)).thenReturn(savedCustomer);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Wonderland"));
    }

    @Test
    void updateCustomerFound() throws Exception {
        UUID id = UUID.randomUUID();
        Customer existingCustomer = new Customer(id, "Old", "Name");
        Customer updatedCustomer = new Customer(id, "New", "Name");

        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        Mockito.when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);

        mockMvc.perform(put("/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("Name"));
    }

    @Test
    void updateCustomerNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Customer updatedCustomer = new Customer(id, "New", "Name");

        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(put("/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCustomerFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(customerRepository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(customerRepository).deleteById(id);

        mockMvc.perform(delete("/customers/{id}", id))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteCustomerNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(customerRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/customers/{id}", id))
                .andExpect(status().isNotFound());
    }

}
