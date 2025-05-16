package com.example.springboot.refresher.controller;

import com.example.springboot.refresher.persistence.model.Customer;
import com.example.springboot.refresher.persistence.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void setupCustomers() {
        if (customerRepository.count() < 50_000) {
            List<Customer> customers = IntStream.range(0, 50_000)
                    .mapToObj(i -> {
                        Customer c = new Customer();
                        c.setFirstName(RandomStringUtils.randomAlphabetic(6));
                        c.setLastName(RandomStringUtils.randomAlphabetic(8));
                        return c;
                    })
                    .toList();

            customerRepository.saveAll(customers);
            System.out.println("50,000 customers created on startup.");
        }
    }

    // Get all customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        customerRepository.save(customer);
        return ResponseEntity.ok(customer);
    }

    // Update an existing customer
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID id, @RequestBody Customer updatedCustomer) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(updatedCustomer.getFirstName());
                    customer.setLastName(updatedCustomer.getLastName());
                    customerRepository.save(customer);
                    return ResponseEntity.ok(customer);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
