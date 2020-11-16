package com.buidangkhoa.daffodil.health_check

import cats.Applicative
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class HeathCheckResponse(
                             status: Boolean,
                             details: Seq[HealthCheckResult]
                             )
object HeathCheckResponse {
  implicit def healthCheckEncoder: Encoder[HeathCheckResponse] = deriveEncoder[HeathCheckResponse]
  implicit def healthCheckEntityEncoder[F[_]: Applicative]: EntityEncoder[F, HeathCheckResponse] = {
    jsonEncoderOf[F, HeathCheckResponse]
  }
}
