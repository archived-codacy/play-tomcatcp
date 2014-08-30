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

    p.setUrl(config.getString("url").getOrElse(""))
    p.setDriverClassName(config.getString("driver").getOrElse(""))
    p.setUsername(config.getString("user").getOrElse(""))
    p.setPassword(config.getString("password").getOrElse(""))
    p.setJdbcInterceptors(config.getString("jdbcInterceptors").getOrElse(""))
    p.setInitialSize(config.getInt("initialSize").getOrElse(0))
    p.setMinIdle(config.getInt("minIdle").getOrElse(0))
    p.setMaxIdle(config.getInt("maxIdle").getOrElse(0))
    p.setMaxActive(config.getInt("maxActive").getOrElse(0))
    p.setMaxWait(config.getInt("maxWait").getOrElse(0))
    p.setJmxEnabled(config.getBoolean("jmxEnabled").getOrElse(false))
    p.setRemoveAbandoned(config.getBoolean("removeAbandoned").getOrElse(false))
    p.setRemoveAbandonedTimeout(config.getInt("removeAbandonedTimeout").getOrElse(0))
    p.setLogAbandoned(config.getBoolean("logAbandoned").getOrElse(false))
    p.setTestOnBorrow(config.getBoolean("testOnBorrow").getOrElse(false))
    p.setTestOnReturn(config.getBoolean("testOnReturn").getOrElse(false))
    p.setTestWhileIdle(config.getBoolean("testWhileIdle").getOrElse(false))
    p.setUseEquals(config.getBoolean("useEquals").getOrElse(false))
    p.setFairQueue(config.getBoolean("fairQueue").getOrElse(false))
    p.setTimeBetweenEvictionRunsMillis(config.getInt("timeBetweenEvictionRunsMillis").getOrElse(0))
    p.setMinEvictableIdleTimeMillis(config.getInt("minEvictableIdleTimeMillis").getOrElse(0))
    p.setValidationInterval(config.getLong("validationInterval").getOrElse(0))
    p.setValidationQuery(config.getString("validationQuery").getOrElse(""))
    p.setDefaultTransactionIsolation(config.getInt("defaultTransactionIsolation").getOrElse(0))

    p
  }

}
