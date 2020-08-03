package models

import java.sql.Timestamp
import play.api.libs.json._

case class EventType(id: Option[Int],
                     title: String,
                     createdAt: Option[Timestamp],
                     updatedAt: Option[Timestamp],
                     deletedAt: Option[Timestamp])

object EventType extends TimestampFormat {
  implicit val writes: OWrites[EventType] = Json.writes[EventType]
  implicit val reads: Reads[EventType] = Json.reads[EventType]
}
