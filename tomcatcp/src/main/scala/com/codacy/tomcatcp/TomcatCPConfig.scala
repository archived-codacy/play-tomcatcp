package com.codacy.tomcatcp

import com.typesafe.config.Config
import com.typesafe.config.ConfigException.Missing
import net.ceedubs.ficus.Ficus._
import org.apache.tomcat.jdbc.pool.PoolProperties

object TomcatCPConfig {

  def getConfig(config: Config): PoolProperties = {
    val p = new PoolProperties

    Seq("url", "driver", "password")
      .foreach { path =>
        if (config.as[Option[String]](path).isEmpty) {
          throw new Missing(path)
        }
      }

    config.as[Option[String]]("url").foreach(p.setUrl)
    config.as[Option[String]]("driver").foreach(p.setDriverClassName)
    config.as[Option[String]]("password").foreach(p.setPassword)

    val userNameValues = Seq("username", "user")
      .flatMap(config.as[Option[String]](_))

    if (userNameValues.isEmpty) {
      throw new Missing(userNameValues.head)
    }

    userNameValues.headOption.foreach(p.setUsername)

    config.as[Option[Config]]("tomcatcp").foreach {
      tomcatConfig =>
        tomcatConfig.as[Option[String]]("jdbcInterceptors").foreach(p.setJdbcInterceptors)
        tomcatConfig.as[Option[Int]]("initialSize").foreach(p.setInitialSize)
        tomcatConfig.as[Option[Int]]("minIdle").foreach(p.setMinIdle)
        tomcatConfig.as[Option[Int]]("maxIdle").foreach(p.setMaxIdle)
        tomcatConfig.as[Option[Int]]("maxActive").foreach(p.setMaxActive)
        tomcatConfig.as[Option[Int]]("maxWait").foreach(p.setMaxWait)
        tomcatConfig.as[Option[Boolean]]("jmxEnabled").foreach(p.setJmxEnabled)
        tomcatConfig.as[Option[Boolean]]("removeAbandoned").foreach(p.setRemoveAbandoned)
        tomcatConfig.as[Option[Int]]("removeAbandonedTimeout").foreach(p.setRemoveAbandonedTimeout)
        tomcatConfig.as[Option[Boolean]]("logAbandoned").foreach(p.setLogAbandoned)
        tomcatConfig.as[Option[Boolean]]("testOnBorrow").foreach(p.setTestOnBorrow)
        tomcatConfig.as[Option[Boolean]]("testOnReturn").foreach(p.setTestOnReturn)
        tomcatConfig.as[Option[Boolean]]("testWhileIdle").foreach(p.setTestWhileIdle)
        tomcatConfig.as[Option[Boolean]]("useEquals").foreach(p.setUseEquals)
        tomcatConfig.as[Option[Boolean]]("fairQueue").foreach(p.setFairQueue)
        tomcatConfig.as[Option[Int]]("timeBetweenEvictionRunsMillis").foreach(p.setTimeBetweenEvictionRunsMillis)
        tomcatConfig.as[Option[Int]]("minEvictableIdleTimeMillis").foreach(p.setMinEvictableIdleTimeMillis)
        tomcatConfig.as[Option[Long]]("validationInterval").foreach(p.setValidationInterval)
        tomcatConfig.as[Option[String]]("validationQuery").foreach(p.setValidationQuery)
        tomcatConfig.as[Option[Int]]("defaultTransactionIsolation").foreach(p.setDefaultTransactionIsolation)
        tomcatConfig.as[Option[Int]]("validationQueryTimeout").foreach(p.setValidationQueryTimeout)
        tomcatConfig.as[Option[Boolean]]("defaultAutoCommit").foreach(p.setDefaultAutoCommit(_))
        tomcatConfig.as[Option[Boolean]]("defaultReadOnly").foreach(p.setDefaultReadOnly(_))
        tomcatConfig.as[Option[String]]("defaultCatalog").foreach(p.setDefaultCatalog)
        tomcatConfig.as[Option[String]]("driverClassName").foreach(p.setDriverClassName)
        tomcatConfig.as[Option[Boolean]]("testOnConnect").foreach(p.setTestOnConnect)
        tomcatConfig.as[Option[String]]("validatorClassName").foreach(p.setValidatorClassName)
        tomcatConfig.as[Option[Int]]("numTestsPerEvictionRun").foreach(p.setNumTestsPerEvictionRun)
        tomcatConfig.as[Option[Boolean]]("accessToUnderlyingConnectionAllowed").foreach(p.setAccessToUnderlyingConnectionAllowed)
        tomcatConfig.as[Option[String]]("connectionProperties").foreach(p.setConnectionProperties)
        tomcatConfig.as[Option[String]]("initSQL").foreach(p.setInitSQL)
        tomcatConfig.as[Option[Int]]("abandonWhenPercentageFull").foreach(p.setAbandonWhenPercentageFull)
        tomcatConfig.as[Option[Long]]("maxAge").foreach(p.setMaxAge)
        tomcatConfig.as[Option[Int]]("suspectTimeout").foreach(p.setSuspectTimeout)
        tomcatConfig.as[Option[Boolean]]("rollbackOnReturn").foreach(p.setRollbackOnReturn)
        tomcatConfig.as[Option[Boolean]]("commitOnReturn").foreach(p.setCommitOnReturn)
        tomcatConfig.as[Option[Boolean]]("alternateUsernameAllowed").foreach(p.setAlternateUsernameAllowed)
        tomcatConfig.as[Option[Int]]("dataSource").foreach(p.setDataSource)
        tomcatConfig.as[Option[String]]("dataSourceJNDI").foreach(p.setDataSourceJNDI)
        tomcatConfig.as[Option[Boolean]]("useDisposableConnectionFacade").foreach(p.setUseDisposableConnectionFacade)
        tomcatConfig.as[Option[Boolean]]("logValidationErrors").foreach(p.setLogValidationErrors)
        tomcatConfig.as[Option[Boolean]]("propagateInterruptState").foreach(p.setPropagateInterruptState)
        tomcatConfig.as[Option[Boolean]]("ignoreExceptionOnPreLoad").foreach(p.setIgnoreExceptionOnPreLoad)
    }

    p
  }
}
