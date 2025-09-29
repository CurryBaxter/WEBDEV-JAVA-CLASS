package exam.views.customers;

import exam.api.Customer;
import exam.views.BaseView;

import java.util.List;

public class CustomerListView extends BaseView {

    private final List<Customer> customers;
    private final String message;

    public CustomerListView(List<Customer> customers, String message) {
        super("list.ftl", "Kundenübersicht - CRM", "/ui/customers");
        this.customers = customers;
        this.message = message;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public String getMessage() {
        return message;
    }
}