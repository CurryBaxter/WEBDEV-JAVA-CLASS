package com.customer.persistence.repo;

import com.customer.persistence.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByName(String name);

    List<Customer> findByStatus(Customer.Status status);

    List<Customer> findByCustomerType(Customer.CustomerType customerType);

    @Query("""
            SELECT c FROM Customer c
            WHERE LOWER(c.name) LIKE :pattern
               OR LOWER(c.contactPerson) LIKE :pattern
               OR LOWER(COALESCE(c.email, '')) LIKE :pattern
               OR LOWER(COALESCE(c.phone, '')) LIKE :pattern
               OR LOWER(COALESCE(c.industry, '')) LIKE :pattern
               OR LOWER(COALESCE(c.address, '')) LIKE :pattern
            ORDER BY c.id
            """)
    List<Customer> searchByPattern(@Param("pattern") String pattern);
}