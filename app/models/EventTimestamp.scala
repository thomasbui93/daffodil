package models

case class EventTimestamp(id: Int,
                          title: String,
                          description: String,
                          event: Event,
                          createdAt: java.sql.Timestamp,
                          deletedAt: Option[java.sql.Timestamp])
