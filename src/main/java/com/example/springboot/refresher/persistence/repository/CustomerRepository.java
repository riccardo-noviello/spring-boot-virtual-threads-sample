package com.example.springboot.refresher.persistence.repository;

import com.example.springboot.refresher.persistence.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    List<Customer> findByLastName(String lastName);

    Optional<Customer> findById(UUID id);
}
