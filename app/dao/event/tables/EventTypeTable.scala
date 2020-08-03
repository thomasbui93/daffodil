package dao.event.tables

import java.sql.Timestamp
import models.EventType
import slick.sql.SqlProfile.ColumnOption.SqlType
import slick.jdbc.MySQLProfile.api._

class EventTypeTable(tag: Tag) extends Table[EventType](tag, "event_types") {
  def id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def title: Rep[String] = column[String]("title")
  def createdAt: Rep[Timestamp] = column[Timestamp]("createdAt", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
  def updatedAt: Rep[Timestamp] = column[Timestamp]("updatedAt", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
  def deletedAt: Rep[Option[Timestamp]] = column[Option[Timestamp]]("deletedAt")
  // scalastyle:off
  def * = (id, title, createdAt.?, updatedAt.?, deletedAt) <> ((EventType.apply _).tupled, EventType.unapply)
  // scalastyle:on
}
