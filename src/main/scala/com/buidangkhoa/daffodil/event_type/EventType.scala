package com.buidangkhoa.daffodil.event_type

import cats.Applicative
import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class EventType(
                    id: Int,
                    title: String,
                    createdAt: String,
                    updatedAt: String
                    )

object EventType {
  implicit val encoder: Encoder[EventType] = deriveEncoder[EventType]
  implicit def entityEncoder[F[_]: Applicative]: EntityEncoder[F, EventType] = {
    jsonEncoderOf[F, EventType]
  }

  implicit val decoder: Decoder[EventType] = deriveDecoder[EventType]
  implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, EventType] = {
    jsonOf[F, EventType]
  }
}
