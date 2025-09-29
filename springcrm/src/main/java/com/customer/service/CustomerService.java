package com.customer.service;

import com.customer.dto.CustomerRequest;
import com.customer.dto.CustomerResponse;
import com.customer.persistence.model.Customer;
import com.customer.persistence.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerResponse> getAllCustomers() {
        return getAllCustomers(null);
    }

    public List<CustomerResponse> getAllCustomers(String query) {
        List<Customer> customers;
        if (hasSearchQuery(query)) {
            String pattern = "%" + query.trim().toLowerCase() + "%";
            customers = customerRepository.searchByPattern(pattern);
        } else {
            customers = customerRepository.findAll();
        }

        return customers.stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());
    }

    public long getTotalCustomerCount() {
        return customerRepository.count();
    }

    private boolean hasSearchQuery(String query) {
        return query != null && !query.trim().isEmpty();
    }

    public Optional<CustomerResponse> getCustomerById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive number");
        }
        return customerRepository.findById(id)
                .map(CustomerResponse::new);
    }

    public CustomerResponse createCustomer(@Valid CustomerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Customer request cannot be null");
        }

        Customer customer = convertToEntity(request);
        
        if (customer.getStatus() == null) {
            customer.setStatus(Customer.Status.LEAD);
        }
        
        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponse(savedCustomer);
    }

    public Optional<CustomerResponse> updateCustomer(Long id, @Valid CustomerRequest request) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive number");
        }
        if (request == null) {
            throw new IllegalArgumentException("Customer request cannot be null");
        }

        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    Customer updatedCustomer = convertToEntity(request);
                    updatedCustomer.setId(id);
                    Customer savedCustomer = customerRepository.save(updatedCustomer);
                    return new CustomerResponse(savedCustomer);
                });
    }

    public boolean deleteCustomer(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive number");
        }

        return customerRepository.findById(id)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return true;
                })
                .orElse(false);
    }

    private Customer convertToEntity(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setContactPerson(request.getContactPerson());
        customer.setAddress(request.getAddress());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCustomerType(request.getCustomerType());
        customer.setIndustry(request.getIndustry());
        customer.setLastContactDate(request.getLastContactDate());
        customer.setStatus(request.getStatus());
        customer.setWantsToBeContactedBy(request.getWantsToBeContactedBy());
        return customer;
    }
}