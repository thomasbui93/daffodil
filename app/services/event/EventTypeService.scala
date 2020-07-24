package services.event

import dao.event.EventTypeDAO
import javax.inject.Inject
import models.EventType
import scala.concurrent.{ExecutionContext, Future}

class EventTypeService @Inject()(dao: EventTypeDAO, implicit val ex: ExecutionContext){
  def create(title: String): Future[EventType] = dao.insert(title)
  def list(search: Option[String], page: Int = 0, size: Int = 10): Future[Seq[EventType]] = dao.queries(search, page, size)
  def count(search: Option[String], page: Int = 0, size: Int = 10): Future[Int] = dao.count(search, page, size)
  def read(id: Int): Future[Option[EventType]] = dao.getOne(id)
  def remove(id: Int, isHardRemove: Boolean): Future[Either[String, Int]] = {
    val removal = if (isHardRemove) dao.deleteOne(id) else dao.softDelete(id)
    removal.map {
      case 0 => Left(s"Item with $id not found")
      case _ => Right(id)
    }
  }

  def update(id: Int, eventType: EventType): Future[Option[EventType]] = dao
    .update(id, eventType)
    .flatMap {
      case 0 => Future.failed(new Exception(s"Failed to updated item at $id"))
      case _ => dao.getOne(id)
    }
}
