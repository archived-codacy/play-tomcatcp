package com.codacy.play.tomcatcp.pool;

import com.codacy.play.tomcatcp.metrics.MetricsTrackerFactory;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;

import java.sql.SQLException;

public class TomcatCPDataSource extends DataSource {

    private MetricsTrackerFactory metricsTrackerFactory;
    private MetricRegistry metricRegistry;
    private HealthCheckRegistry healthCheckRegistry;
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


    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    public void setMetricRegistry(MetricRegistry metricRegistry) {
        if (metricsTrackerFactory != null) {
            throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
        }

        boolean isAlreadySet = getMetricRegistry() != null;

        if (getPool() != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricRegistry can only be set one time");
            } else {
                TCPool.setMetricRegistry(metricRegistry);
            }
        }

        this.metricRegistry = metricRegistry;
    }

    public MetricsTrackerFactory getMetricsTrackerFactory() {
        return metricsTrackerFactory;
    }

    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        if (metricRegistry != null) {
            throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
        }

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

    public HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }

    public void setHealthCheckRegistry(HealthCheckRegistry healthCheckRegistry) {
        boolean isAlreadySet = getHealthCheckRegistry() != null;

        if (getPool() != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("HealthCheckRegistry can only be set one time");
            } else {
                TCPool.setHealthCheckRegistry(healthCheckRegistry);
            }
        }

        this.healthCheckRegistry = healthCheckRegistry;
    }

}
