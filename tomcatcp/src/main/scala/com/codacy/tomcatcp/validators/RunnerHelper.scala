package com.codacy.tomcatcp.validators

import java.util.concurrent.{LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit}

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Try
import scala.util.control.NonFatal

object RunnerHelper {

  private implicit val runnerContext: ExecutionContextExecutorService = {
    val nThreads = 100
    val tp = new ThreadPoolExecutor(nThreads, nThreads, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue[Runnable])
    tp.allowCoreThreadTimeOut(true)

    ExecutionContext.fromExecutorService(tp)
  }

  def runWithTimeout[A](duration: Duration)(block: => A): Try[A] = {
    val futureBlock = Future {
      block
    }.recoverWith {
      case NonFatal(e) =>
        Future.failed(e)
    }

    Try {
      Await.result(futureBlock, duration)
    }
  }

}
