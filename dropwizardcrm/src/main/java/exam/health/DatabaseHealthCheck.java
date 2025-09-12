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
            jdbi.withHandle(handle -> handle.execute("SELECT 1"));
            return Result.healthy("Database connection is healthy.");
        } catch (Exception e) {
            return Result.unhealthy("Cannot connect to the database: " + e.getMessage());
        }
    }
}
