package com.buidangkhoa.daffodil.health_check

import cats.effect._
import doobie.util.transactor.Transactor

trait HealthCheckService[F[_]] {
  def healthCheck(): F[HeathCheckResponse]
}

class HealthCheckServiceImpl(transactor: Transactor[IO]) extends HealthCheckService[IO] {
  def healthCheck(): IO[HeathCheckResponse] = {
    for {
      mySQL <- new MySQLHealthCheck(transactor).check()
      details = List(mySQL)
      summary = summarize(details)
    } yield HeathCheckResponse(summary, details)
  }

  def summarize(components: Seq[HealthCheckResult]): Boolean = {
    components.forall(component => component.status)
  }
}
