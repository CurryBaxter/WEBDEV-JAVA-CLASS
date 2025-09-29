package exam.resources;

import exam.api.Customer;
import exam.db.CustomerDAO;
import exam.views.HomeView;
import exam.views.TestValidationView;
import exam.views.customers.CustomerDetailView;
import exam.views.customers.CustomerFormView;
import exam.views.customers.CustomerListView;
import exam.views.forms.CustomerForm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/ui")
@Produces(MediaType.TEXT_HTML)
public class CustomerViewResource {

    private final CustomerDAO customerDAO;
    private final Validator validator;

    private static final Map<String, String> CUSTOMER_TYPE_OPTIONS =
            Collections.unmodifiableMap(createCustomerTypeOptions());
    private static final Map<String, String> STATUS_OPTIONS =
            Collections.unmodifiableMap(createStatusOptions());
    private static final Map<String, String> CONTACT_METHOD_OPTIONS =
            Collections.unmodifiableMap(createContactMethodOptions());

    public CustomerViewResource(CustomerDAO customerDAO, Validator validator) {
        this.customerDAO = customerDAO;
        this.validator = validator;
    }

    @GET
    public HomeView home() {
        return new HomeView(customerDAO.count());
    }

    @GET
    @Path("/test-validation")
    public TestValidationView testValidation() {
        return new TestValidationView();
    }

    @GET
    @Path("/customers")
    public CustomerListView listCustomers(@QueryParam("message") String message) {
        return new CustomerListView(customerDAO.findAll(), message);
    }

    @GET
    @Path("/customers/new")
    public CustomerFormView newCustomerForm() {
        CustomerForm form = new CustomerForm();
        form.setStatus(Customer.Status.LEAD.name());
        form.setCustomerType(Customer.CustomerType.SOLE_PROPRIETORSHIP.name());
        form.setWantsToBeContactedBy(new ArrayList<>());
        return buildFormView(form, false, Collections.emptyMap(), Collections.emptyList());
    }

    @GET
    @Path("/customers/{id}")
    public Response viewCustomer(@PathParam("id") long id) {
        return customerDAO.findById(id)
                .map(customer -> Response.ok(new CustomerDetailView(customer, CONTACT_METHOD_OPTIONS)).build())
                .orElseGet(() -> redirectToList("Kunde nicht gefunden"));
    }

    @GET
    @Path("/customers/{id}/edit")
    public Response editCustomerForm(@PathParam("id") long id) {
        Optional<Customer> existing = customerDAO.findById(id);
        if (existing.isEmpty()) {
            return redirectToList("Kunde nicht gefunden");
        }
        CustomerForm form = new CustomerForm();
        form.populateFromCustomer(existing.get());
        return Response.ok(buildFormView(form, true, Collections.emptyMap(), Collections.emptyList())).build();
    }

    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createCustomer(@BeanParam CustomerForm form) {
        Map<String, List<String>> fieldErrors = new LinkedHashMap<>();
        List<String> globalErrors = new ArrayList<>();

        Customer customer = buildCustomerFromForm(form, fieldErrors, globalErrors);
        customer.setId(0);

        addViolations(fieldErrors, validator.validate(customer));

        if (!fieldErrors.isEmpty() || !globalErrors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildFormView(form, false, fieldErrors, globalErrors))
                    .build();
        }

        Customer saved = customerDAO.save(customer);
        return redirectToList("Kunde „" + saved.getName() + "“ erfolgreich erstellt");
    }

    @POST
    @Path("/customers/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateCustomer(@PathParam("id") long id, @BeanParam CustomerForm form) {
        Optional<Customer> existing = customerDAO.findById(id);
        if (existing.isEmpty()) {
            return redirectToList("Kunde nicht gefunden");
        }

        form.setId(id);

        Map<String, List<String>> fieldErrors = new LinkedHashMap<>();
        List<String> globalErrors = new ArrayList<>();

        Customer customer = buildCustomerFromForm(form, fieldErrors, globalErrors);
        customer.setId(id);

        addViolations(fieldErrors, validator.validate(customer));

        if (!fieldErrors.isEmpty() || !globalErrors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildFormView(form, true, fieldErrors, globalErrors))
                    .build();
        }

        customerDAO.save(customer);
        return redirectToList("Kunde „" + customer.getName() + "“ wurde aktualisiert");
    }

    @POST
    @Path("/customers/{id}/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteCustomer(@PathParam("id") long id) {
        Optional<Customer> existing = customerDAO.findById(id);
        if (existing.isEmpty()) {
            return redirectToList("Kunde nicht gefunden");
        }

        customerDAO.deleteById(id);
        return redirectToList("Kunde „" + existing.get().getName() + "“ erfolgreich gelöscht");
    }

    @POST
    @Path("/test-force-error")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void forceError(@FormParam("errorType") String errorType) {
        String normalized = errorType == null ? "generic" : errorType.toLowerCase(Locale.ROOT);
        switch (normalized) {
            case "illegal-argument":
                throw new IllegalArgumentException("Absichtlich ausgelöste IllegalArgumentException für Testzwecke");
            case "null-pointer":
                throw new NullPointerException("Absichtlich ausgelöste NullPointerException für Testzwecke");
            case "generic":
            default:
                throw new RuntimeException("Absichtlich ausgelöste RuntimeException für Testzwecke");
        }
    }

    private CustomerFormView buildFormView(CustomerForm form,
                                           boolean edit,
                                           Map<String, List<String>> fieldErrors,
                                           List<String> globalErrors) {
        return new CustomerFormView(
                form,
                edit,
                fieldErrors,
                globalErrors,
                CUSTOMER_TYPE_OPTIONS,
                STATUS_OPTIONS,
                CONTACT_METHOD_OPTIONS
        );
    }

    private Customer buildCustomerFromForm(CustomerForm form,
                                           Map<String, List<String>> fieldErrors,
                                           List<String> globalErrors) {
        Customer customer = new Customer();
        customer.setName(trimToNull(form.getName()));
        customer.setContactPerson(trimToNull(form.getContactPerson()));
        customer.setAddress(trim(form.getAddress()));
        customer.setEmail(trimToNull(form.getEmail()));
        customer.setPhone(trimToNull(form.getPhone()));
        customer.setIndustry(trim(form.getIndustry()));

        String typeValue = trimToNull(form.getCustomerType());
        if (typeValue == null) {
            addFieldError(fieldErrors, "customerType", "Kundentyp ist erforderlich");
        } else {
            try {
                customer.setCustomerType(Customer.CustomerType.valueOf(typeValue));
            } catch (IllegalArgumentException ex) {
                addFieldError(fieldErrors, "customerType", "Ungültiger Kundentyp");
            }
        }

        String statusValue = trimToNull(form.getStatus());
        if (statusValue == null) {
            addFieldError(fieldErrors, "status", "Status ist erforderlich");
        } else {
            try {
                customer.setStatus(Customer.Status.valueOf(statusValue));
            } catch (IllegalArgumentException ex) {
                addFieldError(fieldErrors, "status", "Ungültiger Status");
            }
        }

        String lastContact = trim(form.getLastContactDate());
        if (lastContact != null && !lastContact.isEmpty()) {
            try {
                customer.setLastContactDate(LocalDate.parse(lastContact));
            } catch (DateTimeParseException ex) {
                addFieldError(fieldErrors, "lastContactDate", "Ungültiges Datum (Format: yyyy-MM-dd)");
            }
        } else {
            customer.setLastContactDate(null);
        }

        List<String> contactMethods = new ArrayList<>(form.getWantsToBeContactedBy().stream()
                .map(CustomerViewResource::trimToNull)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        if (contactMethods.size() > 10) {
            addFieldError(fieldErrors, "wantsToBeContactedBy", "Es sind maximal 10 Kontaktpräferenzen erlaubt");
        }

        for (String method : contactMethods) {
            if (!CONTACT_METHOD_OPTIONS.containsKey(method)) {
                addFieldError(fieldErrors, "wantsToBeContactedBy", "Unbekannte Kontaktpräferenz: " + method);
            }
        }

        customer.setWantsToBeContactedBy(contactMethods);

        if (customer.getPhone() == null) {
            addFieldError(fieldErrors, "phone", "Telefonnummer ist erforderlich");
        }

        return customer;
    }

    private void addViolations(Map<String, List<String>> fieldErrors,
                               Set<ConstraintViolation<Customer>> violations) {
        for (ConstraintViolation<Customer> violation : violations) {
            addFieldError(fieldErrors,
                    simplifyPropertyPath(violation.getPropertyPath().toString()),
                    violation.getMessage());
        }
    }

    private static void addFieldError(Map<String, List<String>> fieldErrors, String field, String message) {
        fieldErrors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
    }

    private static String simplifyPropertyPath(String path) {
        String simplified = path;
        if (simplified.contains(".")) {
            simplified = simplified.substring(simplified.lastIndexOf('.') + 1);
        }
        if (simplified.contains("[")) {
            simplified = simplified.substring(0, simplified.indexOf('['));
        }
        return simplified;
    }

    private Response redirectToList(String message) {
        UriBuilder builder = UriBuilder.fromPath("/ui/customers");
        if (message != null && !message.isBlank()) {
            builder.queryParam("message", message);
        }
        return Response.seeOther(builder.build()).build();
    }

    private static Map<String, String> createCustomerTypeOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put(Customer.CustomerType.SOLE_PROPRIETORSHIP.name(), "Einzelunternehmen");
        options.put(Customer.CustomerType.LIMITED_LIABILITY_COMPANY.name(), "Limited Liability Company (LLC)");
        options.put(Customer.CustomerType.CORPORATION.name(), "Aktiengesellschaft");
        options.put(Customer.CustomerType.NON_PROFIT_ORGANIZATION.name(), "Gemeinnützige Organisation");
        return options;
    }

    private static Map<String, String> createStatusOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        for (Customer.Status status : Customer.Status.values()) {
            options.put(status.name(), status.name().substring(0, 1) + status.name().substring(1).toLowerCase(Locale.GERMAN));
        }
        return options;
    }

    private static Map<String, String> createContactMethodOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("EMAIL", "E-Mail");
        options.put("PHONE", "Telefon");
        options.put("VISIT", "Vor-Ort-Termin");
        options.put("VIDEO_CALL", "Video-Call");
        options.put("CHAT", "Live-Chat");
        return options;
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    private static String trimToNull(String value) {
        String trimmed = trim(value);
        return (trimmed == null || trimmed.isEmpty()) ? null : trimmed;
    }
}