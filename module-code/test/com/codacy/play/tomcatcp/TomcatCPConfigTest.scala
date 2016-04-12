package com.codacy.play.tomcatcp

import helpers.TestHelper
import org.scalatest._
import play.api.Configuration

class TomcatCPConfigTest extends FlatSpec with Matchers {

  val config = TestHelper.fakeApplication(Map(
    "url" -> "test",
    "driver" -> "test",
    "username" -> "test",
    "password" -> "test"
  )).configuration

  val result = TomcatCPConfig.getConfig(config)

  "TomcatCPConfig" should "have url and user" in {
    config.getString("url") should be(Some(result.getUrl))
    config.getString("username") should be(Some(result.getUsername))
  }

  it should "get data from config" in {
    val configWithData = config ++ Configuration.from(Map(
      "tomcatcp" -> Map(
        "minIdle" -> 13,
        "maxIdle" -> 33,
        "jmxEnabled" -> false
      )
    ))

    configWithData.getConfig("tomcatcp").map {
      tomcatConfig =>
        tomcatConfig.getInt("minIdle") should be(Some(13))
        tomcatConfig.getInt("maxIdle") should be(Some(33))
        tomcatConfig.getBoolean("jmxEnabled") should be(Some(false))
        true
    }.getOrElse(assert(configWithData.getConfig("tomcatcp").nonEmpty))
  }
}
