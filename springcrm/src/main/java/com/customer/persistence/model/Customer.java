package com.customer.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    private String address;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    @NotNull
    private CustomerType customerType;

    private String industry;

    @Column(name = "last_contact_date")
    private LocalDate lastContactDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Status status;

    @ElementCollection
    @CollectionTable(name = "customer_contact_methods", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "contact_method")
    private List<String> wantsToBeContactedBy;

    public enum Status {
        LEAD, COLD, WARM, CUSTOMER, CLOSED
    }

    public enum CustomerType {
        SOLE_PROPRIETORSHIP,
        LIMITED_LIABILITY_COMPANY
    }

    // Constructors
    public Customer() {}

    public Customer(long id, String name, String contactPerson, String address, String email, String phone,
                    CustomerType customerType, String industry, LocalDate lastContactDate, Status status,
                    List<String> wantsToBeContactedBy) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.customerType = customerType;
        this.industry = industry;
        this.lastContactDate = lastContactDate;
        this.status = status;
        this.wantsToBeContactedBy = wantsToBeContactedBy;
    }

    // Getters and Setters
    @JsonProperty
    public long getId() { return id; }
    
    @JsonProperty
    public void setId(long id) { this.id = id; }
    
    @JsonProperty
    public String getName() { return name; }
    
    @JsonProperty
    public void setName(String name) { this.name = name; }
    
    @JsonProperty
    public String getContactPerson() { return contactPerson; }
    
    @JsonProperty
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    
    @JsonProperty
    public String getAddress() { return address; }
    
    @JsonProperty
    public void setAddress(String address) { this.address = address; }
    
    @JsonProperty
    public String getEmail() { return email; }
    
    @JsonProperty
    public void setEmail(String email) { this.email = email; }
    
    @JsonProperty
    public String getPhone() { return phone; }
    
    @JsonProperty
    public void setPhone(String phone) { this.phone = phone; }
    
    @JsonProperty
    public CustomerType getCustomerType() { return customerType; }
    
    @JsonProperty
    public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }
    
    @JsonProperty
    public String getIndustry() { return industry; }
    
    @JsonProperty
    public void setIndustry(String industry) { this.industry = industry; }
    
    @JsonProperty
    public LocalDate getLastContactDate() { return lastContactDate; }
    
    @JsonProperty
    public void setLastContactDate(LocalDate lastContactDate) { this.lastContactDate = lastContactDate; }
    
    @JsonProperty
    public Status getStatus() { return status; }
    
    @JsonProperty
    public void setStatus(Status status) { this.status = status; }
    
    @JsonProperty
    public List<String> getWantsToBeContactedBy() { return wantsToBeContactedBy; }
    
    @JsonProperty
    public void setWantsToBeContactedBy(List<String> wantsToBeContactedBy) { this.wantsToBeContactedBy = wantsToBeContactedBy; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Objects.equals(name, customer.name) &&
                Objects.equals(contactPerson, customer.contactPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contactPerson);
    }
}