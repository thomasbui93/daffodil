package models

case class Event(id: Int,
                 title: String,
                 description: String,
                 eventType: EventType,
                 createdAt: java.sql.Timestamp,
                 deletedAt: Option[java.sql.Timestamp])
