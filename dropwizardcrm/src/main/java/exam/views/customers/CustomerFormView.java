package exam.views.customers;

import exam.views.BaseView;
import exam.views.forms.CustomerForm;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomerFormView extends BaseView {

    private final CustomerForm form;
    private final boolean edit;
    private final Map<String, List<String>> fieldErrors;
    private final List<String> globalErrors;
    private final Map<String, String> customerTypeOptions;
    private final Map<String, String> statusOptions;
    private final Map<String, String> contactMethodOptions;

    public CustomerFormView(CustomerForm form,
                            boolean edit,
                            Map<String, List<String>> fieldErrors,
                            List<String> globalErrors,
                            Map<String, String> customerTypeOptions,
                            Map<String, String> statusOptions,
                            Map<String, String> contactMethodOptions) {
        super("form.ftl",
                edit ? "Kunde bearbeiten - CRM" : "Kunde anlegen - CRM",
                "/ui/customers");
        this.form = form;
        this.edit = edit;
        this.fieldErrors = fieldErrors == null ? Collections.emptyMap() : fieldErrors;
        this.globalErrors = globalErrors == null ? Collections.emptyList() : globalErrors;
        this.customerTypeOptions = customerTypeOptions;
        this.statusOptions = statusOptions;
        this.contactMethodOptions = contactMethodOptions;
    }

    public CustomerForm getForm() {
        return form;
    }

    public boolean isEdit() {
        return edit;
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }

    public List<String> getGlobalErrors() {
        return globalErrors;
    }

    public Map<String, String> getCustomerTypeOptions() {
        return customerTypeOptions;
    }

    public Map<String, String> getStatusOptions() {
        return statusOptions;
    }

    public Map<String, String> getContactMethodOptions() {
        return contactMethodOptions;
    }
}