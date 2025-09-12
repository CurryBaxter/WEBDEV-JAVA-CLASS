package com.customer.controller;

import com.customer.persistence.model.Customer;
import com.customer.persistence.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(c -> ResponseEntity.ok().body(c))
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@NotNull @Valid @RequestBody Customer customer) {
        customer.setId(0);
        
        if (customer.getStatus() == null) {
            customer.setStatus(Customer.Status.LEAD);
        }
        
        Customer savedCustomer = customerRepository.save(customer);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, 
                                                  @NotNull @Valid @RequestBody Customer customer) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    customer.setId(id);
                    Customer updatedCustomer = customerRepository.save(customer);
                    return ResponseEntity.ok(updatedCustomer);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
