package helpers

import org.scalatest.FlatSpec
import play.api.test.FakeApplication

object TestHelper extends FlatSpec {

  def fakeApplication(moreConfiguration: Map[String, String]) = {
    new FakeApplication(additionalConfiguration = Map(
      "application.mode" -> "test",
      "codacy.discovery.enabled" -> "false"
    ) ++ moreConfiguration)
  }
}
