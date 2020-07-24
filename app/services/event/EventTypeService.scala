package services.event

import dao.event.EventTypeDAO
import javax.inject.Inject
import models.EventType

import scala.concurrent.{ExecutionContext, Future}

class EventTypeService @Inject()(dao: EventTypeDAO, implicit val ex: ExecutionContext){
  def create(title: String): Future[EventType] = dao.insert(title)
  def list(search: Option[String], page: Int = 0, size: Int = 10): Future[Seq[EventType]] = dao.queries(search, page, size)
  def read(id: Int): Future[Option[EventType]] = dao.getOne(id)
  def remove(id: Int): Future[Either[String, Int]] = dao.deleteOne(id).map {
    case 0 => Left(s"Item with $id not found")
    case _ => Right(id)
  }
  def softRemove(id: Int): Future[Option[EventType]] = {
    dao.softDelete(id)
      .flatMap(dao.getOne)
  }
}
