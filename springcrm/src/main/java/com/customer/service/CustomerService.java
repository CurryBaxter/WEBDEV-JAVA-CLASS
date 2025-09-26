package com.customer.service;

import com.customer.dto.CustomerRequest;
import com.customer.dto.CustomerResponse;
import com.customer.persistence.model.Customer;
import com.customer.persistence.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<CustomerResponse> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerResponse::new);
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = convertToEntity(request);
        
        if (customer.getStatus() == null) {
            customer.setStatus(Customer.Status.LEAD);
        }
        
        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponse(savedCustomer);
    }

    public Optional<CustomerResponse> updateCustomer(Long id, CustomerRequest request) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    Customer updatedCustomer = convertToEntity(request);
                    updatedCustomer.setId(id);
                    Customer savedCustomer = customerRepository.save(updatedCustomer);
                    return new CustomerResponse(savedCustomer);
                });
    }

    public boolean deleteCustomer(Long id) {
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