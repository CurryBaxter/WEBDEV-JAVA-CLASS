package com.customer.controller;

import com.customer.dto.CustomerRequest;
import com.customer.dto.CustomerResponse;
import com.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerWebController.class);

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listCustomers(Model model, @RequestParam(required = false) String message) {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "customers/list";
    }

    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable Long id, Model model) {
        logger.debug("Viewing customer with ID: {}", id);
        Optional<CustomerResponse> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            return "customers/detail";
        } else {
            return "redirect:/customers?message=Customer not found";
        }
    }

    @GetMapping("/new")
    public String newCustomerForm(Model model) {
        logger.debug("Showing new customer form");
        model.addAttribute("customer", new CustomerRequest());
        model.addAttribute("isEdit", false);
        return "customers/form";
    }

    @PostMapping
    public String createCustomer(@Valid @ModelAttribute CustomerRequest customer, 
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        
        logger.debug("POST /customers called");
        logger.debug("Customer data received: name={}, contactPerson={}, email={}, phone={}, customerType={}, status={}", 
                    customer.getName(), customer.getContactPerson(), customer.getEmail(), 
                    customer.getPhone(), customer.getCustomerType(), customer.getStatus());
        
        logger.debug("Validation errors count: {}", result.getErrorCount());
        
        // Force WebExceptionHandler to trigger for testing when name is "FORCE_ERROR"
        if ("FORCE_ERROR".equals(customer.getName())) {
            logger.debug("Forcing exception to test WebExceptionHandler");
            throw new IllegalArgumentException("Forced error for testing debug information - name cannot be FORCE_ERROR");
        }
        
        if (result.hasErrors()) {
            logger.debug("Validation errors found:");
            result.getAllErrors().forEach(error -> 
                logger.debug("Error: {}", error.getDefaultMessage())
            );
            
            model.addAttribute("customer", customer);
            model.addAttribute("isEdit", false);
            return "customers/form";
        }

        try {
            CustomerResponse createdCustomer = customerService.createCustomer(customer);
            redirectAttributes.addAttribute("message", "Customer '" + createdCustomer.getName() + "' created successfully");
            return "redirect:/customers";
        } catch (Exception e) {
            logger.error("Error creating customer", e);
            model.addAttribute("customer", customer);
            model.addAttribute("isEdit", false);
            model.addAttribute("error", "Error creating customer: " + e.getMessage());
            return "customers/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editCustomerForm(@PathVariable Long id, Model model) {
        Optional<CustomerResponse> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            model.addAttribute("customer", convertToRequest(customer.get()));
            model.addAttribute("isEdit", true);
            return "customers/form";
        } else {
            return "redirect:/customers?message=Customer not found";
        }
    }

    @PostMapping("/{id}")
    public String updateCustomer(@PathVariable Long id, @Valid @ModelAttribute CustomerRequest customer,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        
        logger.debug("POST /customers/{} called", id);
        logger.debug("Update customer data: name={}, contactPerson={}, email={}, phone={}", 
                    customer.getName(), customer.getContactPerson(), customer.getEmail(), customer.getPhone());
        
        if (result.hasErrors()) {
            logger.debug("Update validation errors found:");
            result.getAllErrors().forEach(error -> 
                logger.debug("Error: {}", error.getDefaultMessage())
            );
            
            customer.setId(id); // Ensure ID is set for edit form
            model.addAttribute("customer", customer);
            model.addAttribute("isEdit", true);
            return "customers/form";
        }

        try {
            Optional<CustomerResponse> updatedCustomer = customerService.updateCustomer(id, customer);
            if (updatedCustomer.isPresent()) {
                redirectAttributes.addAttribute("message", "Customer '" + updatedCustomer.get().getName() + "' updated successfully");
                return "redirect:/customers";
            } else {
                return "redirect:/customers?message=Customer not found";
            }
        } catch (Exception e) {
            logger.error("Error updating customer", e);
            customer.setId(id);
            model.addAttribute("customer", customer);
            model.addAttribute("isEdit", true);
            model.addAttribute("error", "Error updating customer: " + e.getMessage());
            return "customers/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<CustomerResponse> customer = customerService.getCustomerById(id);
            if (customerService.deleteCustomer(id)) {
                redirectAttributes.addAttribute("message", 
                    "Customer" + (customer.isPresent() ? " '" + customer.get().getName() + "'" : "") + " deleted successfully");
            } else {
                redirectAttributes.addAttribute("message", "Customer not found");
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", "Error deleting customer: " + e.getMessage());
        }
        return "redirect:/customers";
    }

    private CustomerRequest convertToRequest(CustomerResponse response) {
        CustomerRequest request = new CustomerRequest();
        request.setId(response.getId());
        request.setName(response.getName());
        request.setContactPerson(response.getContactPerson());
        request.setAddress(response.getAddress());
        request.setEmail(response.getEmail());
        request.setPhone(response.getPhone());
        request.setCustomerType(response.getCustomerType());
        request.setIndustry(response.getIndustry());
        request.setLastContactDate(response.getLastContactDate());
        request.setStatus(response.getStatus());
        request.setWantsToBeContactedBy(response.getWantsToBeContactedBy());
        return request;
    }
}