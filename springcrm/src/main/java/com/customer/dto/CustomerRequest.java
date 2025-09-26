package com.customer.dto;

import com.customer.persistence.model.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class CustomerRequest {

    @NotEmpty(message = "Name is required")
    @JsonProperty
    private String name;

    @NotEmpty(message = "Contact person is required")
    @JsonProperty
    private String contactPerson;

    @JsonProperty
    private String address;

    @Email(message = "Email should be valid")
    @JsonProperty
    private String email;

    @JsonProperty
    private String phone;

    @NotNull(message = "Customer type is required")
    @JsonProperty
    private Customer.CustomerType customerType;

    @JsonProperty
    private String industry;

    @JsonProperty
    private LocalDate lastContactDate;

    @NotNull(message = "Status is required")
    @JsonProperty
    private Customer.Status status;

    @JsonProperty
    private List<String> wantsToBeContactedBy;

    // Constructors
    public CustomerRequest() {}

    // Getters and Setters
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