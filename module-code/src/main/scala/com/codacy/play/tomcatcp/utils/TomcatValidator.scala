package com.codacy.play.tomcatcp.utils

import java.sql.Connection

import org.apache.tomcat.jdbc.pool.Validator

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{Failure, Success}

//Custom validator to force the timeout on the validation query
class TomcatValidator extends Validator {

  def validate(connection: Connection, validateAction: Int): Boolean = {
    val timeout = Duration(5, SECONDS)
    val query = "SELECT 1;"

    RunnerHelper.runWithTimeout(timeout) {
      val stmt = connection.createStatement
      val result = stmt.execute(query)
      stmt.close()
      result
    } match {
      case Success(exitValue) => exitValue
      case Failure(e: TimeoutException) =>
        false
      case Failure(e) =>
        false
    }
  }

}
