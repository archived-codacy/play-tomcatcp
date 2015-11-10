package com.codacy.play.tomcatcp

import helpers.TestHelper
import org.scalatest._
import play.api.Configuration

class TomcatCPConfigTest extends FlatSpec with Matchers {

  val config = TestHelper.fakeApplication.configuration ++ Configuration.from(Map(
    "url" -> "test",
    "driver" -> "test",
    "user" -> "test",
    "password" -> "test"
  ))

  val result = TomcatCPConfig.getConfig(config)

  "TomcatCPConfig" should "have url and user" in {
    config.getString("url") should be(Some(result.getUrl))
    config.getString("user") should be(Some(result.getUsername))
  }

  it should "get data from config" in {
    val configWithData = config ++ Configuration.from(Map(
      "minIdle" -> 13,
      "maxIdle" -> 33,
      "jmxEnabled" -> false
    ))

    configWithData.getInt("minIdle") should be(Some(13))
    configWithData.getInt("maxIdle") should be(Some(33))
    configWithData.getBoolean("jmxEnabled") should be(Some(false))
  }
}
