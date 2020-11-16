package com.buidangkhoa.daffodil.health_check

import cats.effect.IO
import doobie.util.transactor.Transactor
import doobie.implicits._
import com.typesafe.scalalogging.LazyLogging

object MySQLHealthCheck extends LazyLogging {
  def check(transactor: Transactor[IO]): IO[HealthCheckResult] = {
    sql"""SELECT 1 + 1""".query[Int].unique
      .transact(transactor)
      .map(_ => HealthCheckResult(name = "MySQL", status = true, "Connected"))
      .handleErrorWith(ex => {
        for {
          _ <- IO(logger.error(s"Failed to connect: ${ex.getMessage}"))
          resp = HealthCheckResult(
            name = "MySQL",
            status = false,
            "Unable to connect"
          )
        } yield resp
      })
  }
}
