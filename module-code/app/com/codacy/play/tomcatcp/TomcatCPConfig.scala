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

    if (config.getString("url").isEmpty
      || config.getString("driver").isEmpty
      || config.getString("username").isEmpty
      || config.getString("password").isEmpty) {
      throw config.reportError("Play Config", "Required property not found")
    }

    config.getString("url").foreach(p.setUrl)
    config.getString("driver").foreach(p.setDriverClassName)
    config.getString("username").foreach(p.setUsername)
    config.getString("password").foreach(p.setPassword)

    config.getConfig("tomcatcp").foreach {
      tomcatConfig =>

        tomcatConfig.getString("jdbcInterceptors").foreach(p.setJdbcInterceptors)
        tomcatConfig.getInt("initialSize").foreach(p.setInitialSize)
        tomcatConfig.getInt("minIdle").foreach(p.setMinIdle)
        tomcatConfig.getInt("maxIdle").foreach(p.setMaxIdle)
        tomcatConfig.getInt("maxActive").foreach(p.setMaxActive)
        tomcatConfig.getInt("maxWait").foreach(p.setMaxWait)
        tomcatConfig.getBoolean("jmxEnabled").foreach(p.setJmxEnabled)
        tomcatConfig.getBoolean("removeAbandoned").foreach(p.setRemoveAbandoned)
        tomcatConfig.getInt("removeAbandonedTimeout").foreach(p.setRemoveAbandonedTimeout)
        tomcatConfig.getBoolean("logAbandoned").foreach(p.setLogAbandoned)
        tomcatConfig.getBoolean("testOnBorrow").foreach(p.setTestOnBorrow)
        tomcatConfig.getBoolean("testOnReturn").foreach(p.setTestOnReturn)
        tomcatConfig.getBoolean("testWhileIdle").foreach(p.setTestWhileIdle)
        tomcatConfig.getBoolean("useEquals").foreach(p.setUseEquals)
        tomcatConfig.getBoolean("fairQueue").foreach(p.setFairQueue)
        tomcatConfig.getInt("timeBetweenEvictionRunsMillis").foreach(p.setTimeBetweenEvictionRunsMillis)
        tomcatConfig.getInt("minEvictableIdleTimeMillis").foreach(p.setMinEvictableIdleTimeMillis)
        tomcatConfig.getLong("validationInterval").foreach(p.setValidationInterval)
        tomcatConfig.getString("validationQuery").foreach(p.setValidationQuery)
        tomcatConfig.getString("defaultTransactionIsolation").foreach(p.setDefaultTransactionIsolation)
        tomcatConfig.getInt("validationQueryTimeout").foreach(p.setValidationQueryTimeout)
        tomcatConfig.getBoolean("defaultAutoCommit").foreach(p.setDefaultAutoCommit(_))
        tomcatConfig.getBoolean("defaultReadOnly").foreach(p.setDefaultReadOnly(_))
        tomcatConfig.getString("defaultCatalog").foreach(p.setDefaultCatalog)
        tomcatConfig.getString("driverClassName").foreach(p.setDriverClassName)
        tomcatConfig.getBoolean("testOnConnect").foreach(p.setTestOnConnect)
        tomcatConfig.getString("validatorClassName").foreach(p.setValidatorClassName)
        tomcatConfig.getInt("numTestsPerEvictionRun").foreach(p.setNumTestsPerEvictionRun)
        tomcatConfig.getBoolean("accessToUnderlyingConnectionAllowed").foreach(p.setAccessToUnderlyingConnectionAllowed)
        tomcatConfig.getString("connectionProperties").foreach(p.setConnectionProperties)
        tomcatConfig.getString("initSQL").foreach(p.setInitSQL)
        tomcatConfig.getInt("abandonWhenPercentageFull").foreach(p.setAbandonWhenPercentageFull)
        tomcatConfig.getLong("maxAge").foreach(p.setMaxAge)
        tomcatConfig.getInt("suspectTimeout").foreach(p.setSuspectTimeout)
        tomcatConfig.getBoolean("rollbackOnReturn").foreach(p.setRollbackOnReturn)
        tomcatConfig.getBoolean("commitOnReturn").foreach(p.setCommitOnReturn)
        tomcatConfig.getBoolean("alternateUsernameAllowed").foreach(p.setAlternateUsernameAllowed)
        tomcatConfig.getInt("dataSource").foreach(p.setDataSource)
        tomcatConfig.getString("dataSourceJNDI").foreach(p.setDataSourceJNDI)
        tomcatConfig.getBoolean("useDisposableConnectionFacade").foreach(p.setUseDisposableConnectionFacade)
        tomcatConfig.getBoolean("logValidationErrors").foreach(p.setLogValidationErrors)
        tomcatConfig.getBoolean("propagateInterruptState").foreach(p.setPropagateInterruptState)
        tomcatConfig.getBoolean("ignoreExceptionOnPreLoad").foreach(p.setIgnoreExceptionOnPreLoad)
    }

    p
  }

}
