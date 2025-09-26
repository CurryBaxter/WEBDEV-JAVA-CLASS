package exam.db;

import exam.api.Customer;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(CustomerMapper.class)
public interface CustomerDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS customers (" +
            "id BIGSERIAL PRIMARY KEY," +
            "name VARCHAR(100) NOT NULL," +
            "contact_person VARCHAR(100) NOT NULL," +
            "address VARCHAR(255)," +
            "email VARCHAR(100)," +
            "phone VARCHAR(20)," +
            "customer_type VARCHAR(50) NOT NULL," +
            "industry VARCHAR(100)," +
            "last_contact_date DATE," +
            "status VARCHAR(20) NOT NULL," +
            "wants_to_be_contacted_by TEXT" +
            ")")
    void createTable();

    @SqlQuery("SELECT * FROM customers ORDER BY id")
    List<Customer> findAll();

    @SqlQuery("SELECT * FROM customers WHERE id = :id")
    Optional<Customer> findById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM customers WHERE name ILIKE '%' || :name || '%'")
    List<Customer> findByNameContaining(@Bind("name") String name);

    @SqlQuery("SELECT * FROM customers WHERE status = :status")
    List<Customer> findByStatus(@Bind("status") String status);

    @SqlQuery("SELECT * FROM customers WHERE customer_type = :customerType")
    List<Customer> findByCustomerType(@Bind("customerType") String customerType);

    @SqlUpdate("INSERT INTO customers (name, contact_person, address, email, phone, customer_type, industry, last_contact_date, status, wants_to_be_contacted_by) " +
            "VALUES (:name, :contactPerson, :address, :email, :phone, :customerType, :industry, :lastContactDate, :status, :wantsToBeContactedByString)")
    @org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
    Customer insert(@BindBean Customer customer);

    @SqlUpdate("UPDATE customers SET " +
            "name = :name, " +
            "contact_person = :contactPerson, " +
            "address = :address, " +
            "email = :email, " +
            "phone = :phone, " +
            "customer_type = :customerType, " +
            "industry = :industry, " +
            "last_contact_date = :lastContactDate, " +
            "status = :status, " +
            "wants_to_be_contacted_by = :wantsToBeContactedByString " +
            "WHERE id = :id")
    int update(@BindBean Customer customer);

    @SqlUpdate("DELETE FROM customers WHERE id = :id")
    int deleteById(@Bind("id") long id);

    default Customer save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        // Validate required fields
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (customer.getContactPerson() == null || customer.getContactPerson().trim().isEmpty()) {
            throw new IllegalArgumentException("Contact person is required");
        }
        if (customer.getCustomerType() == null) {
            throw new IllegalArgumentException("Customer type is required");
        }
        if (customer.getStatus() == null) {
            throw new IllegalArgumentException("Status is required");
        }

        // Set default status if not provided
        if (customer.getStatus() == null) {
            customer.setStatus(Customer.Status.LEAD);
        }

        // Prepare contact methods string
        if (customer.getWantsToBeContactedBy() != null) {
            customer.setWantsToBeContactedByString(String.join(",", customer.getWantsToBeContactedBy()));
        }

        if (customer.getId() == 0) {
            return insert(customer);
        } else {
            update(customer);
            return customer;
        }
    }
}
