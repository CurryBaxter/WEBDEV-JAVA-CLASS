package exam.views.customers;

import exam.api.Customer;
import exam.views.BaseView;

import java.util.Map;

public class CustomerDetailView extends BaseView {

    private final Customer customer;
    private final Map<String, String> contactMethodOptions;

    public CustomerDetailView(Customer customer, Map<String, String> contactMethodOptions) {
        super("detail.ftl", "Kundendetails - CRM", "/ui/customers");
        this.customer = customer;
        this.contactMethodOptions = contactMethodOptions;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Map<String, String> getContactMethodOptions() {
        return contactMethodOptions;
    }
}