package exam.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Represents a customer in the CRM system.
 * This class is used for API requests and responses.
 */
public class Customer {

    @JsonProperty
    private long id;

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

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]+$", message = "Phone number format is invalid")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @JsonProperty
    private String phone;

    @NotNull(message = "Customer type is required")
    @JsonProperty
    private CustomerType customerType;

    @Size(max = 100, message = "Industry must not exceed 100 characters")
    @JsonProperty
    private String industry;

    @PastOrPresent(message = "Last contact date cannot be in the future")
    @JsonProperty
    private LocalDate lastContactDate;

    @NotNull(message = "Status is required")
    @JsonProperty
    private Status status;

    @Size(max = 10, message = "Maximum 10 contact methods allowed")
    @JsonProperty
    private List<@NotEmpty(message = "Contact method cannot be empty") String> wantsToBeContactedBy;

    // Helper field for database operations (not exposed in JSON)
    @JsonIgnore
    private String wantsToBeContactedByString;

    public enum Status {
        LEAD, COLD, WARM, CUSTOMER, CLOSED
    }

    public enum CustomerType {
        SOLE_PROPRIETORSHIP,
        LIMITED_LIABILITY_COMPANY,
        CORPORATION,
        NON_PROFIT_ORGANIZATION
    }

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

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

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

    public CustomerType getCustomerType() { return customerType; }
    public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public LocalDate getLastContactDate() { return lastContactDate; }
    public void setLastContactDate(LocalDate lastContactDate) { this.lastContactDate = lastContactDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<String> getWantsToBeContactedBy() { return wantsToBeContactedBy; }
    public void setWantsToBeContactedBy(List<String> wantsToBeContactedBy) { this.wantsToBeContactedBy = wantsToBeContactedBy; }

    // Helper methods for database operations
    public String getWantsToBeContactedByString() { return wantsToBeContactedByString; }
    public void setWantsToBeContactedByString(String wantsToBeContactedByString) { this.wantsToBeContactedByString = wantsToBeContactedByString; }

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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", customerType=" + customerType +
                ", status=" + status +
                '}';
    }
}
