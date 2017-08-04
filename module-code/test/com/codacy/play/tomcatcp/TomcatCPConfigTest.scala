package com.codacy.play.tomcatcp

import java.io.File

import org.apache.tomcat.jdbc.pool.PoolProperties
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.ApplicationLoader.Context
import play.api._
import play.api.mvc.EssentialFilter
import play.api.routing.Router

trait CustomPlaySpec extends PlaySpec with GuiceOneAppPerSuite {
  self =>

  protected lazy val context: Context = ApplicationLoader.createContext(
    new Environment(new File("."), ApplicationLoader.getClass.getClassLoader, Mode.Test)
  )

  protected lazy val components: BuiltInComponentsFromContext = {
    new BuiltInComponentsFromContext(context) {
      override lazy val configuration: Configuration = testConfiguration

      override def router: Router = Router.empty

      override def httpFilters: Seq[EssentialFilter] = Seq.empty
    }
  }

  protected lazy val testConfiguration: Configuration = context.initialConfiguration ++ Configuration.from(
    Map(
      "application.mode" -> "test",
      "codacy.discovery.enabled" -> "false"
    )
  )

  override implicit lazy val app: Application = {
    new ApplicationLoader() {
      override def load(context: Context): Application = {
        components.application
      }
    }.load(context)
  }

}

class TomcatCPConfigTest extends CustomPlaySpec {

  val config: Configuration = components.configuration ++ Configuration.from(Map(
    "url" -> "test",
    "driver" -> "test",
    "username" -> "test",
    "password" -> "test"
  ))

  val result: PoolProperties = TomcatCPConfig.getConfig(config)

  "TomcatCPConfig" should {
    "have url and user" in {
      config.getOptional[String]("url") must be(Some(result.getUrl))
      config.getOptional[String]("username") must be(Some(result.getUsername))
    }
  }

  it should {
    "get data from config" in {
      val configWithData = config ++ Configuration.from(Map(
        "tomcatcp" -> Map(
          "minIdle" -> 13,
          "maxIdle" -> 33,
          "jmxEnabled" -> false
        )
      ))

      configWithData.getOptional[Configuration]("tomcatcp").map {
        tomcatConfig =>
          tomcatConfig.getOptional[Int]("minIdle") must be(Some(13))
          tomcatConfig.getOptional[Int]("maxIdle") must be(Some(33))
          tomcatConfig.getOptional[Boolean]("jmxEnabled") must be(Some(false))
          true
      }.getOrElse(assert(configWithData.getOptional[Configuration]("tomcatcp").nonEmpty))
    }
  }

}
