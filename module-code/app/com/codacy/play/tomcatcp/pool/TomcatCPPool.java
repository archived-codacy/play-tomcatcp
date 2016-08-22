package com.codacy.play.tomcatcp.pool;

import com.codacy.play.tomcatcp.metrics.MetricsTracker;
import com.codacy.play.tomcatcp.metrics.MetricsTrackerFactory;
import com.codacy.play.tomcatcp.metrics.PoolStats;
import com.codacy.play.tomcatcp.metrics.dropwizard.CodahaleMetricsTrackerFactory;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.zaxxer.hikari.metrics.CodahaleHealthChecker;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;

import java.sql.SQLException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TomcatCPPool extends ConnectionPool {
    private boolean isRecordMetrics;
    private MetricsTracker metricsTracker;

    public TomcatCPPool(PoolConfiguration prop) throws SQLException {
        super(prop);
    }

    public void setMetricRegistry(MetricRegistry metricRegistry) {
        this.isRecordMetrics = metricRegistry != null;
        if (isRecordMetrics) {
            setMetricsTrackerFactory(new CodahaleMetricsTrackerFactory(metricRegistry));
        } else {
            setMetricsTrackerFactory(null);
        }
    }

    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        this.isRecordMetrics = metricsTrackerFactory != null;
        if (isRecordMetrics) {
            this.metricsTracker = metricsTrackerFactory.create(getName(), getPoolStats());
        } else {
            this.metricsTracker = new MetricsTracker();
        }
    }

    public void setHealthCheckRegistry(HealthCheckRegistry healthCheckRegistry) {
        if (healthCheckRegistry != null) {
            //CodahaleHealthChecker.registerHealthChecks(this, config, healthCheckRegistry);
        }
    }

    @Override
    protected void close(boolean force) {
        try {
            super.close(force);
        } finally {
            metricsTracker.close();
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
