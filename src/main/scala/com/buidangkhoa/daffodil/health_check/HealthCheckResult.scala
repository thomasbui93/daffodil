package com.buidangkhoa.daffodil.health_check

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class HealthCheckResult(
                            name: String,
                            status: Boolean,
                            message: String
                            )
object HealthCheckResult {
  implicit val greetingEncoder: Encoder[HealthCheckResult] = new Encoder[HealthCheckResult] {
    final def apply(a: HealthCheckResult): Json = {
      Json.obj(
        ("name", Json.fromString(a.name)),
        ("status", Json.fromBoolean(a.status)),
        ("message", Json.fromString(a.message))
      )
    }
  }
  implicit def healthCheckEncoder[F[_]: Applicative]: EntityEncoder[F, HealthCheckResult] = {
    jsonEncoderOf[F, HealthCheckResult]
  }
}
