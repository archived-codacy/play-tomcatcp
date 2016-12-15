package com.codacy.play.tomcatcp

import javax.inject.{Inject, Singleton}
import javax.sql.DataSource

import com.codacy.play.tomcatcp.pool.TomcatCPDataSource
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.health.HealthCheckRegistry
import com.typesafe.config.Config
import play.api._
import play.api.db.{ConnectionPool, DatabaseConfig}
import play.api.libs.JNDI

import scala.util.{Failure, Success, Try}

/**
  * TomcatCP components (for compile-time injection).
  */
trait TomcatCPComponents {
  def metricRegistry: Option[MetricRegistry]

  def healthCheckRegistry: Option[HealthCheckRegistry]

  lazy val connectionPool: ConnectionPool = new TomcatCPConnectionPool(metricRegistry, healthCheckRegistry)
}

@Singleton
class TomcatCPConnectionPool @Inject()(metricRegistry: => Option[MetricRegistry],
                                       healthCheckRegistry: => Option[HealthCheckRegistry]) extends ConnectionPool {

  import TomcatCPConnectionPool._

  override def create(name: String, dbConfig: DatabaseConfig, config: Config): DataSource = {
    Try {

      val tomcatConfig = TomcatCPConfig.getConfig(Configuration(config))
      val dataSource = new TomcatCPDataSource(tomcatConfig)
      metricRegistry.foreach(dataSource.setMetricRegistry)
      healthCheckRegistry.foreach(dataSource.setHealthCheckRegistry)

      logger.info("Starting Tomcat connection pool...")

      dbConfig.jndiName.foreach { jndiName =>
        JNDI.initialContext.rebind(jndiName, dataSource)
        logger.info(s"datasource [$name] bound to JNDI as $jndiName")
      }

      dataSource
    } match {
      case Success(datasource) => datasource
      case Failure(ex) =>
        throw new Configuration(config).reportError(name, ex.getMessage, Some(ex))
    }
  }

  /**
    * Close the given data source.
    *
    * @param dataSource the data source to close
    */
  override def close(dataSource: DataSource) = {
    logger.info("Shutting down connection pool.")
    dataSource match {
      case ds: TomcatCPDataSource => ds.close()
      case _ => sys.error("Unable to close data source: not a Tomcat")
    }
  }
}

object TomcatCPConnectionPool {
  private val logger = Logger(classOf[TomcatCPConnectionPool])
}
