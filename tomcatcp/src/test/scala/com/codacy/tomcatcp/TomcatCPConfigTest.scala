package com.codacy.tomcatcp

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.apache.tomcat.jdbc.pool.PoolProperties
import org.scalatest.{FlatSpec, Matchers}

class TomcatCPConfigTest extends FlatSpec with Matchers {

  val config: Config = ConfigFactory.parseString(
    """
      |"url": "test",
      |"driver": "test",
      |"username": "test",
      |"password": "test"
    """.stripMargin)

  val result: PoolProperties = TomcatCPConfig.getConfig(config)

  "TomcatCPConfig" should "have url and user" in {
    config.as[Option[String]]("url") should be(Some(result.getUrl))
    config.as[Option[String]]("username") should be(Some(result.getUsername))
  }

  it should "get data from config" in {
    val configWithData = config.withFallback(
      ConfigFactory.parseString(
        """
          |"tomcatcp": {
          |  "minIdle": 13,
          |  "maxIdle": 33,
          |  "jmxEnabled": false
          |}
        """.stripMargin))

    val tomcatConfig = configWithData.as[Config]("tomcatcp")
    tomcatConfig.as[Option[Int]]("minIdle") should be(Some(13))
    tomcatConfig.as[Option[Int]]("maxIdle") should be(Some(33))
    tomcatConfig.as[Option[Boolean]]("jmxEnabled") should be(Some(false))
  }

}
