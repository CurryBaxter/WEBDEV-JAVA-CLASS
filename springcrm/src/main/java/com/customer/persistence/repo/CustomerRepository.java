package com.customer.persistence.repo;

import com.customer.persistence.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    List<Customer> findByName(String name);
    List<Customer> findByStatus(Customer.Status status);
    List<Customer> findByCustomerType(Customer.CustomerType customerType);
}