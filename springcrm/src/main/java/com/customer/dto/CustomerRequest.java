package com.customer.dto;

import com.customer.persistence.model.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class CustomerRequest {

    @JsonProperty
    private Long id;

    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @JsonProperty
    private String name;

    @NotEmpty(message = "Contact person is required")
    @Size(min = 2, max = 100, message = "Contact person must be between 2 and 100 characters")
    @JsonProperty
    private String contactPerson;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    @JsonProperty
    private String address;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @JsonProperty
    private String email;

    @NotBlank(message = "Phone number is required")  // Changed from @Size to @NotBlank
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]+$", message = "Phone number format is invalid")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @JsonProperty
    private String phone;

    @NotNull(message = "Customer type is required")
    @JsonProperty
    private Customer.CustomerType customerType;

    @Size(max = 100, message = "Industry must not exceed 100 characters")
    @JsonProperty
    private String industry;

    @PastOrPresent(message = "Last contact date cannot be in the future")
    @JsonProperty
    private LocalDate lastContactDate;

    @NotNull(message = "Status is required")
    @JsonProperty
    private Customer.Status status;

    @Size(max = 10, message = "Maximum 10 contact methods allowed")
    @JsonProperty
    private List<String> wantsToBeContactedBy;

    // Constructors
    public CustomerRequest() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Customer.CustomerType getCustomerType() { return customerType; }
    public void setCustomerType(Customer.CustomerType customerType) { this.customerType = customerType; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public LocalDate getLastContactDate() { return lastContactDate; }
    public void setLastContactDate(LocalDate lastContactDate) { this.lastContactDate = lastContactDate; }

    public Customer.Status getStatus() { return status; }
    public void setStatus(Customer.Status status) { this.status = status; }

    public List<String> getWantsToBeContactedBy() { return wantsToBeContactedBy; }
    public void setWantsToBeContactedBy(List<String> wantsToBeContactedBy) { this.wantsToBeContactedBy = wantsToBeContactedBy; }
}