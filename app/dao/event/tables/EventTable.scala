package dao.event.tables

import java.sql.Timestamp
import models.{Event, EventType}
import slick.sql.SqlProfile.ColumnOption.SqlType
import slick.jdbc.MySQLProfile.api._
import slick.lifted.ForeignKeyQuery

class EventTable(tag: Tag) extends Table[Event](tag, "events") {
  val eventTypes = TableQuery[EventTypeTable]

  def id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def title: Rep[String] = column[String]("title")
  def description: Rep[Option[String]] = column[Option[String]]("description")
  def eventTypeId: Rep[Option[Int]] = column[Option[Int]]("eventTypeId")
  def createdAt: Rep[Timestamp] = column[Timestamp]("createdAt", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
  def updatedAt: Rep[Timestamp] = column[Timestamp]("updatedAt", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
  def deletedAt: Rep[Option[Timestamp]] = column[Option[Timestamp]]("deletedAt")
  // scalastyle:off
  def * = (id, title, createdAt.?, updatedAt.?, deletedAt) <> ((Event.apply _).tupled, Event.unapply)
  // scalastyle:on
  def eventType:ForeignKeyQuery[EventTypeTable, EventType] = {
    foreignKey("fk_event_type", eventTypeId, eventTypes)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }
}
