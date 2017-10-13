package com.codacy.tomcatcp.play

import javax.inject.{Inject, Singleton}
import javax.sql.DataSource

import com.codacy.tomcatcp.TomcatCPConfig
import com.codacy.tomcatcp.metrics.MetricsTrackerFactory
import com.codacy.tomcatcp.pool.TomcatCPDataSource
import com.typesafe.config.Config
import play.api._
import play.api.db.{ConnectionPool, DatabaseConfig}
import play.api.inject.{Binding, Module}
import play.api.libs.JNDI

import scala.util.{Failure, Success, Try}

/**
  * TomcatCP runtime inject module.
  */
class TomcatCPModule extends Module {
  def bindings(environment: Environment, configuration: Configuration): Seq[Binding[ConnectionPool]] = {
    Seq(
      bind[ConnectionPool].to[TomcatCPConnectionPool]
    )
  }
}

/**
  * TomcatCP components (for compile-time injection).
  */
trait TomcatCPComponents {
  def environment: Environment

  def metricsTrackerFactory: Option[MetricsTrackerFactory]

  lazy val connectionPool: ConnectionPool = new TomcatCPConnectionPool(environment, metricsTrackerFactory)
}

@Singleton
class TomcatCPConnectionPool @Inject()(environment: Environment,
                                       metricsTrackerFactory: => Option[MetricsTrackerFactory]) extends ConnectionPool {

  import TomcatCPConnectionPool._

  override def create(name: String, dbConfig: DatabaseConfig, config: Config): DataSource = {
    Try {
      val tomcatConfig = TomcatCPConfig.getConfig(config)
      val dataSource = new TomcatCPDataSource(tomcatConfig)
      metricsTrackerFactory.foreach(dataSource.setMetricsTrackerFactory)

      play.api.Logger.info("Starting Tomcat connection pool...")

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
  override def close(dataSource: DataSource): Unit = {
    Logger.info("Shutting down connection pool.")
    dataSource match {
      case ds: TomcatCPDataSource => ds.close()
      case _ => sys.error("Unable to close data source: not a Tomcat")
    }
  }
}

object TomcatCPConnectionPool {
  private val logger = Logger(classOf[TomcatCPConnectionPool])
}
