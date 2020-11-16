package com.buidangkhoa.daffodil

import cats.effect.IO
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import health_check.HealthCheckService

object DaffodilRoutes {
  def healthCheckRoutes(transactor: Transactor[IO]): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "z" / "ping" =>
        for {
          healthCheck <- HealthCheckService.healthCheck(transactor)
          resp <- Ok(healthCheck)
        } yield resp
    }
  }
}
