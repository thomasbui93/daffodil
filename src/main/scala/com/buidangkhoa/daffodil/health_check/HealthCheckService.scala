package com.buidangkhoa.daffodil.health_check

import cats.effect._
import doobie.util.transactor.Transactor

object HealthCheckService {
  def healthCheck(transactor: Transactor[IO]): IO[HealthCheckResult] = {
    for {
      mySQL <- MySQLHealthCheck.check(transactor)
    } yield mySQL
  }
}
