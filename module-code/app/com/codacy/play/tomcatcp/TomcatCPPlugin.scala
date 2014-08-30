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

import java.sql.Connection

import play.api.db.{DBApi, DBPlugin}
import play.api.{Application, Configuration, Logger, Mode}

import scala.util.control.NonFatal

class TomcatCPPlugin(app: Application) extends DBPlugin {

  lazy val databaseConfig = app.configuration.getConfig("db").getOrElse(Configuration.empty)

  override def enabled = !app.configuration.getString("tomcat.enabled").contains("false")


  private lazy val tomcatCPDBApi = new TomcatCPDBApi(databaseConfig, app.classloader)

  override def api: DBApi = tomcatCPDBApi

  override def onStart() {
    play.api.Logger.info("Starting Tomcat connection pool...")
    tomcatCPDBApi.datasources.map { case (dataSource, dataSourceName) =>
      try {
        dataSource.getConnection.close()
        app.mode match {
          case Mode.Test =>
          case mode: Mode.Value => Logger.info(s"database [$dataSourceName] connected at ${dbURL(dataSource.getConnection)}")
        }
      } catch {
        case NonFatal(e) =>
          throw databaseConfig.reportError(s"$dataSourceName.url", s"Cannot connect to database [$dataSourceName]", Some(e.getCause))
      }
    }
  }

  override def onStop() {
    Logger.info("Stopping Tomcat connection pool...")
    tomcatCPDBApi.datasources.foreach {
      case (ds, _) => try {
        tomcatCPDBApi.shutdownPool(ds)
      } catch {
        case NonFatal(e) =>
          Logger.error(s"Failed to stop Tomcat connection pool: ${e.getCause}")
      }
    }

    tomcatCPDBApi.deregisterAll()
  }

  private def dbURL(conn: Connection): String = {
    val u = conn.getMetaData.getURL
    conn.close()
    u
  }
}
