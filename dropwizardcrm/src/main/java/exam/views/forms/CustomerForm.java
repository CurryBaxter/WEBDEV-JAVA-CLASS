package exam.views.forms;

import exam.api.Customer;
import jakarta.ws.rs.FormParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CustomerForm {

    @FormParam("id")
    private Long id;

    @FormParam("name")
    private String name;

    @FormParam("contactPerson")
    private String contactPerson;

    @FormParam("address")
    private String address;

    @FormParam("email")
    private String email;

    @FormParam("phone")
    private String phone;

    @FormParam("customerType")
    private String customerType;

    @FormParam("industry")
    private String industry;

    @FormParam("lastContactDate")
    private String lastContactDate;

    @FormParam("status")
    private String status;

    @FormParam("wantsToBeContactedBy")
    private List<String> wantsToBeContactedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLastContactDate() {
        return lastContactDate;
    }

    public void setLastContactDate(String lastContactDate) {
        this.lastContactDate = lastContactDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getWantsToBeContactedBy() {
        return wantsToBeContactedBy == null ? Collections.emptyList() : wantsToBeContactedBy;
    }

    public void setWantsToBeContactedBy(List<String> wantsToBeContactedBy) {
        this.wantsToBeContactedBy = wantsToBeContactedBy;
    }

    public void populateFromCustomer(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.contactPerson = customer.getContactPerson();
        this.address = customer.getAddress();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.customerType = customer.getCustomerType() != null ? customer.getCustomerType().name() : null;
        this.industry = customer.getIndustry();
        this.lastContactDate = customer.getLastContactDate() != null ? customer.getLastContactDate().toString() : null;
        this.status = customer.getStatus() != null ? customer.getStatus().name() : null;
        this.wantsToBeContactedBy = new ArrayList<>(Optional.ofNullable(customer.getWantsToBeContactedBy())
                .orElse(Collections.emptyList()));
    }
}