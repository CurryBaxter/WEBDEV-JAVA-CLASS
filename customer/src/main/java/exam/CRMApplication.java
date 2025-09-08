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
        // Application initialization logic goes here.
    }

    @Override
    public void run(final CRMConfiguration configuration,
                    final Environment environment) {
        // 1. Create the JDBI factory to connect to the database using the config.yml file.
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        // 2. Now that you have a `jdbi` object, create an instance of the health check with it.
        final DatabaseHealthCheck databaseHealthCheck = new DatabaseHealthCheck(jdbi);

        // 3. Register the health check with a descriptive name.
        environment.healthChecks().register("database", databaseHealthCheck);

        // 4. Create the JDBI-based DAO (not the old in-memory one).
        final CustomerDAO customerDAO = jdbi.onDemand(CustomerDAO.class);

        // Ensure the database table exists.
        customerDAO.createTable();

        // 5. Create the resource instance, injecting the DAO.
        final CustomerResource customerResource = new CustomerResource(customerDAO);

        // 6. Register the resource with the Jersey environment.
        environment.jersey().register(customerResource);
    }
}

