package models

import java.sql.Timestamp

case class Event(id: Option[Int],
                 title: Option[String],
                 description: Option[String],
                 eventType: Option[Int],
                 createdAt: Option[Timestamp],
                 updatedAt: Option[Timestamp],
                 deletedAt: Option[Timestamp])
