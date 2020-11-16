package com.buidangkhoa.daffodil.health_check

import cats.effect.IO
import doobie.util.transactor.Transactor
import doobie.implicits._

object MySQLHealthCheck {
  def check(transactor: Transactor[IO]): IO[HealthCheckResult] = {
    sql"""SELECT 1 + 1""".query[Int].unique
      .transact(transactor)
      .map(_ => HealthCheckResult(name = "MySQL", status = true, "Connected"))
      .handleErrorWith(ex => IO.pure(HealthCheckResult(
        name = "MySQL",
        status = false,
        s"Unable to connect. Reason: ${ex.getMessage}")
      ))
  }
}
