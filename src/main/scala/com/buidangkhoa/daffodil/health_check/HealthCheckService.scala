package com.buidangkhoa.daffodil.health_check

import cats.effect._
import doobie.util.transactor.Transactor

object HealthCheckService {
  def healthCheck(transactor: Transactor[IO]): IO[HeathCheckResponse] = {
    for {
      mySQL <- MySQLHealthCheck.check(transactor)
      details = List(mySQL)
      summary = summarize(details)
    } yield HeathCheckResponse(summary, details)
  }

  def summarize(components: Seq[HealthCheckResult]): Boolean = {
    components.forall(component => component.status)
  }
}
