package exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;

/**
 * The configuration class for the CRM application.
 * Includes database settings for PostgreSQL.
 */
public class CRMConfiguration extends Configuration {
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}