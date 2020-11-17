package com.buidangkhoa.daffodil.event_type

import cats.Applicative
import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class EventTypeRequest(title: String)

object EventTypeRequest {
  implicit val encoder: Encoder[EventTypeRequest] = deriveEncoder[EventTypeRequest]
  implicit def entityEncoder[F[_]: Applicative]: EntityEncoder[F, EventTypeRequest] = {
    jsonEncoderOf[F, EventTypeRequest]
  }

  implicit val decoder: Decoder[EventTypeRequest] = deriveDecoder[EventTypeRequest]
  implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, EventTypeRequest] = {
    jsonOf[F, EventTypeRequest]
  }
}
