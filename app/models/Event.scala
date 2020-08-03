package models

import java.sql.Timestamp

case class Event(id: Option[Int],
                 title: String,
                 description: String,
                 eventType: EventType,
                 createdAt: Option[Timestamp],
                 updatedAt: Option[Timestamp],
                 deletedAt: Option[Timestamp])
