package com.codacy.play.tomcatcp

import org.scalatest._
import play.api.Configuration

class TomcatCPDBApiTest extends FlatSpec with Matchers {

  "TomcatCPDBApi" should "create on a empty configuration" in {
    new TomcatCPDBApi(Configuration.empty, ClassLoader.getSystemClassLoader)
  }
}
