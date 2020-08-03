package dao.event

import java.sql.Timestamp
import java.time.LocalDateTime
import configuration.PaginationConfig
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import models.EventType
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.sql.SqlProfile.ColumnOption.SqlType
import play.api.Configuration

class EventTypeDAO @Inject()
  (protected val dbConfigProvider: DatabaseConfigProvider, protected val config: Configuration)
  (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  val eventTypeTable = TableQuery[EventTypeTable]
  val pagination: PaginationConfig = config.get[PaginationConfig]("daffodil.model.pagination")

  def queries(search: Option[String], page: Int = pagination.page, size: Int = pagination.size): Future[Seq[EventType]] = db.run {
    eventTypeTable
      .filter(_.title like s"%${search.getOrElse("")}%")
      .drop((page - 1) * size)
      .take(size)
      .result
  }

  def count(search: Option[String]): Future[Int] = db.run {
    eventTypeTable
      .filter(_.title like s"%${search.getOrElse("")}%")
      .length
      .result
  }

  val insertQuery = eventTypeTable returning eventTypeTable.map(_.id) into ((eventType, id) => eventType.copy(id = id))

  def insert(title: String): Future[EventType] = {
    val now = Timestamp.valueOf(LocalDateTime.now())
    val action = insertQuery += EventType(None, title, Some(now), Some(now), None)
    db.run(action)
  }

  def update(id: Int, eventType: EventType): Future[Int] = db.run {
    val now = Timestamp.valueOf(LocalDateTime.now())
    eventTypeTable.filter(_.id === id)
      .map(s => (s.title, s.updatedAt))
      .update((eventType.title, now))
  }

  def getOne(id: Int): Future[Option[EventType]] = db.run {
    eventTypeTable
      .filter(_.id === id)
      .result
      .headOption
  }

  def deleteOne(id: Int): Future[Int] = db.run {
    eventTypeTable
      .filter(_.id === id)
      .delete
  }

  def softDelete(id: Int): Future[Int] = db.run {
    val now = Timestamp.valueOf(LocalDateTime.now())
    val q = for { evenType <- eventTypeTable if evenType.id === id } yield evenType.deletedAt
    q.update(Some(now))
  }

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
}
