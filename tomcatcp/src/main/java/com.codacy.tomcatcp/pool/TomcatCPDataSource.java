package com.codacy.tomcatcp.pool;

import com.codacy.tomcatcp.metrics.MetricsTrackerFactory;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;

import java.sql.SQLException;

public class TomcatCPDataSource extends DataSource {

    private MetricsTrackerFactory metricsTrackerFactory;
    private TomcatCPPool TCPool;

    public TomcatCPDataSource() {
        super();
    }


    public TomcatCPDataSource(PoolConfiguration poolProperties) {
        super(poolProperties);
    }

    @Override
    public ConnectionPool createPool() throws SQLException {
        if (pool != null) {
            return pool;
        } else {
            TCPool = new TomcatCPPool(poolProperties);
            pool = TCPool;
            return pool;
        }
    }

    public MetricsTrackerFactory getMetricsTrackerFactory() {
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
