package services.event

import configuration.PaginationConfig
import dao.event.{EventDAO, EventTypeDAO}
import models.Event
import javax.inject.Inject
import play.api.Configuration

import scala.concurrent.{ExecutionContext, Future}

class EventService @Inject()(eventDao: EventDAO, eventTypeDao: EventTypeDAO, config: Configuration, implicit val ex: ExecutionContext) {
  val p: PaginationConfig = config.get[PaginationConfig]("daffodil.model.pagination")

  def create(payload: Event): Future[Event] = eventDao.insert(payload)
  def fetchByTypeId(typeId: Int, page: Int = p.page, size: Int = p.size): Future[Seq[Event]] = {
    eventTypeDao
      .getOne(typeId)
      .flatMap {
        case Some(_) => eventDao.queries(typeId, page, size)
        case None => Future.failed(new Exception("Event type is not found."))
      }
  }
  def read(id: Int): Future[Option[Event]] = eventDao.getOne(id)
  def remove(id: Int): Future[Either[String, Int]] = {
    eventDao.softDelete(id)
      .map {
        case 0 => Left(s"Item with $id not found")
        case _ => Right(id)
      }
  }
  def update(id: Int, event: Event): Future[Option[Event]] = {
    eventDao
      .update(id, event)
      .flatMap {
        case 0 => Future.failed(new Exception(s"Failed to update item at $id"))
        case _ => eventDao.getOne(id)
      }
  }
}
