/*
 * Copyright 2014 Edulify.com
 * Copyright 2014 Codacy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codacy.play.tomcatcp

import org.apache.tomcat.jdbc.pool.PoolProperties
import play.api.{Configuration, Logger}

object TomcatCPConfig {

  def getConfig(config: Configuration): PoolProperties = {
    Logger.info("Loading Tomcat configuration from Play configuration.")

    val p = new PoolProperties

    if (config.getOptional[String]("url").isEmpty
      || config.getOptional[String]("driver").isEmpty
      || config.getOptional[String]("username").isEmpty
      || config.getOptional[String]("password").isEmpty) {
      throw config.reportError("Play Config", "Required property not found")
    }

    config.getOptional[String]("url").foreach(p.setUrl)
    config.getOptional[String]("driver").foreach(p.setDriverClassName)
    config.getOptional[String]("username").foreach(p.setUsername)
    config.getOptional[String]("password").foreach(p.setPassword)

    config.getOptional[Configuration]("tomcatcp").foreach {
      tomcatConfig =>
        tomcatConfig.getOptional[String]("jdbcInterceptors").foreach(p.setJdbcInterceptors)
        tomcatConfig.getOptional[Int]("initialSize").foreach(p.setInitialSize)
        tomcatConfig.getOptional[Int]("minIdle").foreach(p.setMinIdle)
        tomcatConfig.getOptional[Int]("maxIdle").foreach(p.setMaxIdle)
        tomcatConfig.getOptional[Int]("maxActive").foreach(p.setMaxActive)
        tomcatConfig.getOptional[Int]("maxWait").foreach(p.setMaxWait)
        tomcatConfig.getOptional[Boolean]("jmxEnabled").foreach(p.setJmxEnabled)
        tomcatConfig.getOptional[Boolean]("removeAbandoned").foreach(p.setRemoveAbandoned)
        tomcatConfig.getOptional[Int]("removeAbandonedTimeout").foreach(p.setRemoveAbandonedTimeout)
        tomcatConfig.getOptional[Boolean]("logAbandoned").foreach(p.setLogAbandoned)
        tomcatConfig.getOptional[Boolean]("testOnBorrow").foreach(p.setTestOnBorrow)
        tomcatConfig.getOptional[Boolean]("testOnReturn").foreach(p.setTestOnReturn)
        tomcatConfig.getOptional[Boolean]("testWhileIdle").foreach(p.setTestWhileIdle)
        tomcatConfig.getOptional[Boolean]("useEquals").foreach(p.setUseEquals)
        tomcatConfig.getOptional[Boolean]("fairQueue").foreach(p.setFairQueue)
        tomcatConfig.getOptional[Int]("timeBetweenEvictionRunsMillis").foreach(p.setTimeBetweenEvictionRunsMillis)
        tomcatConfig.getOptional[Int]("minEvictableIdleTimeMillis").foreach(p.setMinEvictableIdleTimeMillis)
        tomcatConfig.getOptional[Long]("validationInterval").foreach(p.setValidationInterval)
        tomcatConfig.getOptional[String]("validationQuery").foreach(p.setValidationQuery)
        tomcatConfig.getOptional[Int]("defaultTransactionIsolation").foreach(p.setDefaultTransactionIsolation)
        tomcatConfig.getOptional[Int]("validationQueryTimeout").foreach(p.setValidationQueryTimeout)
        tomcatConfig.getOptional[Boolean]("defaultAutoCommit").foreach(p.setDefaultAutoCommit(_))
        tomcatConfig.getOptional[Boolean]("defaultReadOnly").foreach(p.setDefaultReadOnly(_))
        tomcatConfig.getOptional[String]("defaultCatalog").foreach(p.setDefaultCatalog)
        tomcatConfig.getOptional[String]("driverClassName").foreach(p.setDriverClassName)
        tomcatConfig.getOptional[Boolean]("testOnConnect").foreach(p.setTestOnConnect)
        tomcatConfig.getOptional[String]("validatorClassName").foreach(p.setValidatorClassName)
        tomcatConfig.getOptional[Int]("numTestsPerEvictionRun").foreach(p.setNumTestsPerEvictionRun)
        tomcatConfig.getOptional[Boolean]("accessToUnderlyingConnectionAllowed").foreach(p.setAccessToUnderlyingConnectionAllowed)
        tomcatConfig.getOptional[String]("connectionProperties").foreach(p.setConnectionProperties)
        tomcatConfig.getOptional[String]("initSQL").foreach(p.setInitSQL)
        tomcatConfig.getOptional[Int]("abandonWhenPercentageFull").foreach(p.setAbandonWhenPercentageFull)
        tomcatConfig.getOptional[Long]("maxAge").foreach(p.setMaxAge)
        tomcatConfig.getOptional[Int]("suspectTimeout").foreach(p.setSuspectTimeout)
        tomcatConfig.getOptional[Boolean]("rollbackOnReturn").foreach(p.setRollbackOnReturn)
        tomcatConfig.getOptional[Boolean]("commitOnReturn").foreach(p.setCommitOnReturn)
        tomcatConfig.getOptional[Boolean]("alternateUsernameAllowed").foreach(p.setAlternateUsernameAllowed)
        tomcatConfig.getOptional[Int]("dataSource").foreach(p.setDataSource)
        tomcatConfig.getOptional[String]("dataSourceJNDI").foreach(p.setDataSourceJNDI)
        tomcatConfig.getOptional[Boolean]("useDisposableConnectionFacade").foreach(p.setUseDisposableConnectionFacade)
        tomcatConfig.getOptional[Boolean]("logValidationErrors").foreach(p.setLogValidationErrors)
        tomcatConfig.getOptional[Boolean]("propagateInterruptState").foreach(p.setPropagateInterruptState)
        tomcatConfig.getOptional[Boolean]("ignoreExceptionOnPreLoad").foreach(p.setIgnoreExceptionOnPreLoad)
    }

    p
  }

}
