package exam;

import exam.db.CustomerDAO;
import exam.exceptions.*;
import exam.health.DatabaseHealthCheck;
import exam.resources.CustomerResource;
import exam.resources.CustomerViewResource;
import io.dropwizard.core.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.views.common.ViewBundle;
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
        bootstrap.addBundle(new ViewBundle<>());
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

        final CustomerViewResource customerViewResource = new CustomerViewResource(customerDAO, environment.getValidator());
        environment.jersey().register(customerViewResource);

        // Register Exception Mappers
        environment.jersey().register(ValidationExceptionMapper.class);
        environment.jersey().register(JsonProcessingExceptionMapper.class);
        environment.jersey().register(IllegalArgumentExceptionMapper.class);
        environment.jersey().register(WebApplicationExceptionMapper.class);
        environment.jersey().register(RuntimeExceptionMapper.class);
        environment.jersey().register(new ProblemDetailHtmlMessageBodyWriter());
    }
}

