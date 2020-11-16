package com.buidangkhoa.daffodil

import cats.effect.{ContextShift, IO}
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import health_check.{HealthCheckResult, HealthCheckService, HeathCheckResponse, MySQLHealthCheck}

class HealthCheckServiceStub extends HealthCheckService[IO] {
  def healthCheck(): IO[HeathCheckResponse] = {
    for {
      mySQL <- IO.pure(HealthCheckResult(name = "MySQL", status = true, "Connected"))
      details = List(mySQL)
      summary = summarize(details)
    } yield HeathCheckResponse(summary, details)
  }

  def summarize(components: Seq[HealthCheckResult]): Boolean = {
    components.forall(component => component.status)
  }
}

class HealthCheckSpec extends org.specs2.mutable.Specification {
  "HealthCheck" >> {
    "return 200" >> {
      uriReturns200()
    }
    "return status true" >> {
      uriReturnsStatusTrue()
    }
  }

  private[this] val retHelloWorld: Response[IO] = {
    val getHW = Request[IO](Method.GET, uri"/z/ping")
    DaffodilRoutes.healthCheckRoutes(new HealthCheckServiceStub()).orNotFound(getHW).unsafeRunSync()
  }

  private[this] def uriReturns200(): MatchResult[Status] =
    retHelloWorld.status must beEqualTo(Status.Ok)

  private[this] def uriReturnsStatusTrue(): MatchResult[String] =
    retHelloWorld.as[String].unsafeRunSync() must beEqualTo("{\"status\":true,\"details\":[{\"name\":\"MySQL\",\"status\":true,\"message\":\"Connected\"}]}")
}
