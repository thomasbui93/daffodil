package models

import java.sql.Timestamp

case class EventType(id: Int,
                     title: String,
                     createdAt: Timestamp,
                     updatedAt: Timestamp,
                     deletedAt: Option[Timestamp])