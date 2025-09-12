package exam.db;

import exam.api.Customer;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(CustomerMapper.class)
public interface CustomerDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS customers (" +
               "id SERIAL PRIMARY KEY, " +
               "name VARCHAR(255) NOT NULL, " +
               "contact_person VARCHAR(255) NOT NULL, " +
               "address VARCHAR(255), " +
               "email VARCHAR(255), " +
               "phone VARCHAR(255), " +
               "customer_type VARCHAR(50) NOT NULL, " +
               "industry VARCHAR(255), " +
               "last_contact_date DATE, " +
               "status VARCHAR(20) NOT NULL, " +
               "wants_to_be_contacted_by TEXT)")
    void createTable();

    @SqlQuery("SELECT * FROM customers WHERE id = :id")
    Optional<Customer> findById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM customers ORDER BY id")
    List<Customer> findAll();

    @SqlUpdate("INSERT INTO customers (name, contact_person, address, email, phone, customer_type, industry, last_contact_date, status, wants_to_be_contacted_by) " +
               "VALUES (:name, :contactPerson, :address, :email, :phone, :customerType, :industry, :lastContactDate, :status, :wantsToBeContactedByAsString)")
    @GetGeneratedKeys
    long insert(@BindBean Customer customer);

    @SqlUpdate("UPDATE customers SET name = :name, contact_person = :contactPerson, address = :address, email = :email, phone = :phone, " +
               "customer_type = :customerType, industry = :industry, last_contact_date = :lastContactDate, status = :status, " +
               "wants_to_be_contacted_by = :wantsToBeContactedByAsString WHERE id = :id")
    void update(@BindBean Customer customer);

    @SqlUpdate("DELETE FROM customers WHERE id = :id")
    int deleteById(@Bind("id") long id);

    // Helper method to save (insert or update)
    default Customer save(Customer customer) {
        if (customer.getId() == 0) {
            if (customer.getStatus() == null) {
                customer.setStatus(Customer.Status.LEAD);
            }
            long generatedId = insert(customer);
            customer.setId(generatedId);
        } else {
            update(customer);
        }
        return customer;
    }
}
