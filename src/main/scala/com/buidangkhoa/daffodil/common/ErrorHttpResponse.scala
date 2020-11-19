package com.buidangkhoa.daffodil.common

import cats.Applicative
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class ErrorHttpResponse(
                        error: Boolean,
                        message: String)

object ErrorHttpResponse {
  implicit def encoder: Encoder[ErrorHttpResponse] = deriveEncoder[ErrorHttpResponse]
  implicit def entityEncoder[F[_]: Applicative]: EntityEncoder[F, ErrorHttpResponse] = {
    jsonEncoderOf[F, ErrorHttpResponse]
  }
}
