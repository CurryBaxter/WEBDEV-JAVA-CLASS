package exam.health;

import com.codahale.metrics.health.HealthCheck;
import org.jdbi.v3.core.Jdbi;

/**
 * A health check for the database connection.
 * It executes a simple query to ensure the connection is alive and valid.
 */
public class DatabaseHealthCheck extends HealthCheck {
    private final Jdbi jdbi;

    public DatabaseHealthCheck(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    protected Result check() throws Exception {
        try {
            // Use JDBI to handle the connection and execute a simple validation query.
            // 'SELECT 1' is a standard, lightweight query supported by most databases.
            jdbi.withHandle(handle -> handle.execute("SELECT 1"));
            return Result.healthy("Database connection is healthy.");
        } catch (Exception e) {
            // If an exception occurs, the health check fails.
            // We include the error message for debugging purposes.
            return Result.unhealthy("Cannot connect to the database: " + e.getMessage());
        }
    }
}
