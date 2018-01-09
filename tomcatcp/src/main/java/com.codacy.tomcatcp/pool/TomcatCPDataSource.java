package com.codacy.tomcatcp.pool;

import com.codacy.tomcatcp.metrics.MetricsTrackerFactory;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;

import java.sql.SQLException;

public class TomcatCPDataSource extends DataSource {

    private MetricsTrackerFactory metricsTrackerFactory;
    private TomcatCPPool TCPool;
    private String databaseName;

    public TomcatCPDataSource(String databaseName) {
        super();
        this.databaseName = databaseName;
    }

    public TomcatCPDataSource(String databaseName, PoolConfiguration poolProperties) {
        super(poolProperties);
        this.databaseName = databaseName;
    }

    @Override
    public ConnectionPool createPool() throws SQLException {
        if (pool != null) {
            return pool;
        } else {
            TCPool = new TomcatCPPool(databaseName, poolProperties);
            pool = TCPool;
            return pool;
        }
    }

    private MetricsTrackerFactory getMetricsTrackerFactory() {
        return metricsTrackerFactory;
    }

    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        boolean isAlreadySet = getMetricsTrackerFactory() != null;

        if (getPool() != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricsTrackerFactory can only be set one time");
            } else {
                TCPool.setMetricsTrackerFactory(metricsTrackerFactory);
            }
        }

        this.metricsTrackerFactory = metricsTrackerFactory;
    }

}
