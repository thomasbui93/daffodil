package com.buidangkhoa.daffodil

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import fs2.Stream
import com.buidangkhoa.daffodil.config.AppConfig

object Main extends IOApp {
  def run(args: List[String]) = {
    val stream = for {
      config <- Stream.eval(AppConfig.load())
      exitCode <- DaffodilServer.stream[IO](config)
    } yield exitCode
    stream.compile.drain.as(ExitCode.Success)
  }
}
