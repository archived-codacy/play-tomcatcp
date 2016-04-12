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

    config.getString("url").map(p.setUrl)
    config.getString("driver").map(p.setDriverClassName)
    config.getString("username").map(p.setUsername)
    config.getString("password").map(p.setPassword)

    config.getConfig("tomcatcp").map {
      tomcatConfig =>

        tomcatConfig.getString("jdbcInterceptors").map(p.setJdbcInterceptors)
        tomcatConfig.getInt("initialSize").map(p.setInitialSize)
        tomcatConfig.getInt("minIdle").map(p.setMinIdle)
        tomcatConfig.getInt("maxIdle").map(p.setMaxIdle)
        tomcatConfig.getInt("maxActive").map(p.setMaxActive)
        tomcatConfig.getInt("maxWait").map(p.setMaxWait)
        tomcatConfig.getBoolean("jmxEnabled").map(p.setJmxEnabled)
        tomcatConfig.getBoolean("removeAbandoned").map(p.setRemoveAbandoned)
        tomcatConfig.getInt("removeAbandonedTimeout").map(p.setRemoveAbandonedTimeout)
        tomcatConfig.getBoolean("logAbandoned").map(p.setLogAbandoned)
        tomcatConfig.getBoolean("testOnBorrow").map(p.setTestOnBorrow)
        tomcatConfig.getBoolean("testOnReturn").map(p.setTestOnReturn)
        tomcatConfig.getBoolean("testWhileIdle").map(p.setTestWhileIdle)
        tomcatConfig.getBoolean("useEquals").map(p.setUseEquals)
        tomcatConfig.getBoolean("fairQueue").map(p.setFairQueue)
        tomcatConfig.getInt("timeBetweenEvictionRunsMillis").map(p.setTimeBetweenEvictionRunsMillis)
        tomcatConfig.getInt("minEvictableIdleTimeMillis").map(p.setMinEvictableIdleTimeMillis)
        tomcatConfig.getLong("validationInterval").map(p.setValidationInterval)
        tomcatConfig.getString("validationQuery").map(p.setValidationQuery)
        tomcatConfig.getInt("defaultTransactionIsolation").map(p.setDefaultTransactionIsolation)
    }

    p
  }

}
