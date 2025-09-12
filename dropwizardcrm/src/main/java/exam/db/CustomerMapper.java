package exam.db;

import exam.api.Customer;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class CustomerMapper implements RowMapper<Customer> {

    @Override
    public Customer map(ResultSet rs, StatementContext ctx) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setName(rs.getString("name"));
        customer.setContactPerson(rs.getString("contact_person"));
        customer.setAddress(rs.getString("address"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));

        // Handle Enum mapping
        customer.setCustomerType(Customer.CustomerType.valueOf(rs.getString("customer_type")));
        customer.setStatus(Customer.Status.valueOf(rs.getString("status")));

        customer.setIndustry(rs.getString("industry"));

        // Handle nullable date
        java.sql.Date dbDate = rs.getDate("last_contact_date");
        if (dbDate != null) {
            customer.setLastContactDate(dbDate.toLocalDate());
        }

        // Handle comma-separated list
        String contactedBy = rs.getString("wants_to_be_contacted_by");
        if (contactedBy != null && !contactedBy.isEmpty()) {
            customer.setWantsToBeContactedBy(Arrays.asList(contactedBy.split(",")));
        } else {
            customer.setWantsToBeContactedBy(Collections.emptyList());
        }

        return customer;
    }
}
