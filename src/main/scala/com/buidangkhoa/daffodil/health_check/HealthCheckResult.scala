package com.buidangkhoa.daffodil.health_check

import cats.Applicative
import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import io.circe.generic.semiauto._

case class HealthCheckResult(
                            name: String,
                            status: Boolean,
                            message: String
                            )
object HealthCheckResult {
  implicit def healthCheckEncoder: Encoder[HealthCheckResult] = deriveEncoder[HealthCheckResult]
  implicit def healthCheckEntityEncoder[F[_]: Applicative]: EntityEncoder[F, HealthCheckResult] = {
    jsonEncoderOf[F, HealthCheckResult]
  }
}
