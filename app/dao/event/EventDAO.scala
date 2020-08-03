package dao.event

import java.sql.Timestamp
import java.time.LocalDateTime
import configuration.PaginationConfig
import dao.event.tables.EventTable
import javax.inject.Inject
import models.Event
import play.api.Configuration
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.event.EventTypeService
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

class EventDAO @Inject()
  (protected val dbConfigProvider: DatabaseConfigProvider,
   protected val config: Configuration,
   protected val eventTypeService: EventTypeService)
  (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  val eventTable = TableQuery[EventTable]
  val p: PaginationConfig = config.get[PaginationConfig]("daffodil.model.pagination")

  def queries(typeId: Int, page: Int = p.page, size: Int = p.size): Future[Seq[Event]] = db.run {
    eventTable
      .filter(_.eventTypeId === typeId)
      .drop((page - 1) * size)
      .take(size)
      .result
  }

  def count(typeId: Int): Future[Int] = db.run {
    eventTable
      .filter(_.eventTypeId === typeId)
      .length
      .result
  }

  def insert(payload: Event): Future[Event] = {
    val insertQuery = eventTable returning eventTable.map(_.id) into ((eventType, id) => eventType.copy(id = id))
    val now = Timestamp.valueOf(LocalDateTime.now())
    val action = insertQuery += Event(
      id = None,
      title = payload.title,
      description = payload.description,
      createdAt = Some(now),
      updatedAt = Some(now),
      deletedAt = None,
      eventType = payload.eventType
    )
    db.run(action)
  }

  def getOne(id: Int): Future[Option[Event]] = db.run {
    eventTable
      .filter(_.id === id)
      .result
      .headOption
  }

  def deleteOne(id: Int): Future[Int] = db.run {
    eventTable
      .filter(_.id === id)
      .delete
  }

  def softDelete(id: Int): Future[Int] = db.run {
    val now = Timestamp.valueOf(LocalDateTime.now())
    val q = for { evenType <- eventTable if evenType.id === id } yield evenType.deletedAt
    q.update(Some(now))
  }

  def update(id: Int, event: Event): Future[Int] = {
    val populatedEventFuture = getOne(id)
    populatedEventFuture.flatMap {
      case Some(populatedEvent) => db.run {
        val now = Timestamp.valueOf(LocalDateTime.now())
        val description = event.description match {
          case Some(value) => Some(value)
          case None => populatedEvent.description
        }
        val title = event.title match {
          case Some(value) => Some(value)
          case None => populatedEvent.title
        }
        eventTable.filter(_.id === event.id)
          .map(s => (s.title, s.description, s.updatedAt))
          .update((title, description, now))
      }
      case None => Future.failed(new Exception("Update is failed"))
    }
  }
}
