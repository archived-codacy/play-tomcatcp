package com.codacy.play.tomcatcp.pool;

import com.codacy.play.tomcatcp.metrics.MetricsTracker;
import com.codacy.play.tomcatcp.metrics.MetricsTrackerFactory;
import com.codacy.play.tomcatcp.metrics.PoolStats;
import com.codacy.play.tomcatcp.metrics.dropwizard.CodaHaleMetricsTrackerFactory;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;

import java.sql.SQLException;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TomcatCPPool extends ConnectionPool {
    private Optional<MetricsTracker> metricsTracker = Optional.empty();

    public TomcatCPPool(PoolConfiguration prop) throws SQLException {
        super(prop);
    }

    public void setMetricRegistry(MetricRegistry metricRegistry) {
        setMetricsTrackerFactory(new CodaHaleMetricsTrackerFactory(metricRegistry));
    }

    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        this.metricsTracker = Optional.of(metricsTrackerFactory.create(getName(), getPoolStats()));
    }

    public void setHealthCheckRegistry(HealthCheckRegistry healthCheckRegistry) {
        //CodaHaleHealthChecker.registerHealthChecks(this, config, healthCheckRegistry);
        // TODO : to be implemented in the future
    }

    @Override
    protected void close(boolean force) {
        try {
            super.close(force);
        } finally {
            metricsTracker.ifPresent(MetricsTracker::close);
        }
    }

    private PoolStats getPoolStats() {
        return new PoolStats(SECONDS.toMillis(1)) {
            @Override
            protected void update() {
                this.pendingThreads = TomcatCPPool.this.getWaitCount();
                this.idleConnections = TomcatCPPool.this.getIdle();
                this.totalConnections = TomcatCPPool.this.getSize();
                this.activeConnections = TomcatCPPool.this.getActive();
            }
        };
    }
}
