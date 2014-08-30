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
      || config.getString("user").isEmpty
      || config.getString("password").isEmpty) {
      throw config.reportError("Play Config", "Required property not found")
    }

    config.getString("url").map(p.setUrl)
    config.getString("driver").map(p.setDriverClassName)
    config.getString("user").map(p.setUsername)
    config.getString("password").map(p.setPassword)

    config.getString("jdbcInterceptors").map(p.setJdbcInterceptors)
    config.getInt("initialSize").map(p.setInitialSize)
    config.getInt("minIdle").map(p.setMinIdle)
    config.getInt("maxIdle").map(p.setMaxIdle)
    config.getInt("maxActive").map(p.setMaxActive)
    config.getInt("maxWait").map(p.setMaxWait)
    config.getBoolean("jmxEnabled").map(p.setJmxEnabled)
    config.getBoolean("removeAbandoned").map(p.setRemoveAbandoned)
    config.getInt("removeAbandonedTimeout").map(p.setRemoveAbandonedTimeout)
    config.getBoolean("logAbandoned").map(p.setLogAbandoned)
    config.getBoolean("testOnBorrow").map(p.setTestOnBorrow)
    config.getBoolean("testOnReturn").map(p.setTestOnReturn)
    config.getBoolean("testWhileIdle").map(p.setTestWhileIdle)
    config.getBoolean("useEquals").map(p.setUseEquals)
    config.getBoolean("fairQueue").map(p.setFairQueue)
    config.getInt("timeBetweenEvictionRunsMillis").map(p.setTimeBetweenEvictionRunsMillis)
    config.getInt("minEvictableIdleTimeMillis").map(p.setMinEvictableIdleTimeMillis)
    config.getLong("validationInterval").map(p.setValidationInterval)
    config.getString("validationQuery").map(p.setValidationQuery)
    config.getInt("defaultTransactionIsolation").map(p.setDefaultTransactionIsolation)

    p
  }

}
