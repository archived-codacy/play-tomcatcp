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

import java.sql.{Driver, DriverManager}
import javax.sql.DataSource

import org.apache.tomcat.jdbc.pool.{DataSource => TomcatDataSource, PoolProperties}
import play.api.db.DBApi
import play.api.libs.JNDI
import play.api.{Configuration, Logger}

import scala.util.control.NonFatal

class TomcatCPDBApi(configuration: Configuration, classloader: ClassLoader) extends DBApi {

  lazy val dataSourceConfigs = configuration.subKeys.map {
    dataSourceName => dataSourceName -> configuration.getConfig(dataSourceName).getOrElse(Configuration.empty)
  }

  val (datasources, drivers): (List[(DataSource, String)], List[Driver]) = {
    val (dataSources, drivers) = dataSourceConfigs.map {
      case (dataSourceName, dataSourceConfig) =>
        Logger.info(s"Creating Pool for datasource: '$dataSourceName'")
        val tomcatConfig = TomcatCPConfig.getConfig(dataSourceConfig)
        val driver = registerDriver(dataSourceConfig)
        val dataSource = new TomcatDataSource(tomcatConfig)
        bindToJNDI(dataSourceConfig, tomcatConfig, dataSource)
        (dataSource -> dataSourceName, driver)
    }.toList.unzip

    (dataSources, drivers.flatten)
  }

  def deregisterAll(): Unit = drivers.foreach(DriverManager.deregisterDriver)

  def shutdownPool(ds: DataSource) = {
    Logger.info("Shutting down connection pool.")
    ds match {
      case ds: TomcatDataSource => ds.close()
    }
  }

  def getDataSource(name: String): DataSource = {
    val dataSource = datasources.find(tuple => tuple._2 == name)
      .map(element => element._1)
      .getOrElse(sys.error(" - could not find datasource for name " + name))
    dataSource
  }

  private def registerDriver(config: Configuration): Option[Driver] = {
    config.getString("driver").map { driverConf =>
      try {
        val driverClassName = driverConf
        Logger.info(s"Registering driver $driverClassName")
        val driver = new play.utils.ProxyDriver(Class.forName(driverClassName, true, classloader).newInstance.asInstanceOf[Driver])
        DriverManager.registerDriver(driver)
        driver
      } catch {
        case NonFatal(e) => throw config.reportError("driver", s"Driver not found: [$driverConf]", Some(e.getCause))
      }
    }
  }

  private def bindToJNDI(config: Configuration, tomcatConfig: PoolProperties, dataSource: DataSource): Unit = {
    config.getString("jndiName") map { name =>
      JNDI.initialContext.rebind(name, dataSource)
      Logger.info( s"""datasource [${tomcatConfig.getUrl}] bound to JNDI as $name""")
    }
  }
}
