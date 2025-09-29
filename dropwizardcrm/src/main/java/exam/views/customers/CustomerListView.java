package exam.views.customers;

import exam.api.Customer;
import exam.views.BaseView;

import java.util.List;
import java.util.Objects;

public class CustomerListView extends BaseView {

    private final List<Customer> customers;
    private final String message;
    private final String searchTerm;
    private final long totalCount;
    private final int resultCount;

    public CustomerListView(List<Customer> customers, String message, String searchTerm, long totalCount) {
        super("list.ftl", "Kundenübersicht - CRM", "/ui/customers");
        this.customers = List.copyOf(Objects.requireNonNull(customers, "customers"));
        this.message = message;
        this.searchTerm = searchTerm;
        this.totalCount = totalCount;
        this.resultCount = this.customers.size();
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public String getMessage() {
        return message;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public int getResultCount() {
        return resultCount;
    }

    public boolean isSearching() {
        return searchTerm != null && !searchTerm.isBlank();
    }
}