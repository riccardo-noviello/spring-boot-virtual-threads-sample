package com.example.springboot.refresher.persistence.repository;

import com.example.springboot.refresher.persistence.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByLastName() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");

        customerRepository.save(customer);

        List<Customer> results = customerRepository.findByLastName("Doe");

        assertThat(results).isNotEmpty();
        assertThat(results.getFirst().getLastName()).isEqualTo("Doe");
    }

    @Test
    void findById() {
        Customer customer = new Customer();
        customer.setFirstName("Jane");
        customer.setLastName("Smith");

        customer = customerRepository.save(customer);
        UUID id = customer.getId();

        Optional<Customer> result = customerRepository.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Jane");
    }
}
