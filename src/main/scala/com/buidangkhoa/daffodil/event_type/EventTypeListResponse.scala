package com.buidangkhoa.daffodil.event_type

import cats.Applicative
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class EventTypeListResponse(eventTypes: Seq[EventType], count: Int)

object EventTypeListResponse {
  implicit val encoder: Encoder[EventTypeListResponse] = deriveEncoder[EventTypeListResponse]
  implicit def entityEncoder[F[_]: Applicative]: EntityEncoder[F, EventTypeListResponse] = {
    jsonEncoderOf[F, EventTypeListResponse]
  }
}
