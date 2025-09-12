package exam;

import exam.db.CustomerDAO;
import exam.health.DatabaseHealthCheck;
import exam.resources.CustomerResource;
import io.dropwizard.core.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class CRMApplication extends Application<CRMConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CRMApplication().run(args);
    }

    @Override
    public String getName() {
        return "CRM";
    }

    @Override
    public void initialize(final Bootstrap<CRMConfiguration> bootstrap) {
    }

    @Override
    public void run(final CRMConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        final DatabaseHealthCheck databaseHealthCheck = new DatabaseHealthCheck(jdbi);

        environment.healthChecks().register("database", databaseHealthCheck);

        final CustomerDAO customerDAO = jdbi.onDemand(CustomerDAO.class);

        customerDAO.createTable();

        final CustomerResource customerResource = new CustomerResource(customerDAO);

        environment.jersey().register(customerResource);
    }
}

