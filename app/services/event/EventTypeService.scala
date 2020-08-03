package services.event

import configuration.PaginationConfig
import dao.event.EventTypeDAO
import javax.inject.Inject
import models.EventType
import play.api.Configuration
import scala.concurrent.{ExecutionContext, Future}

class EventTypeService @Inject()(dao: EventTypeDAO, config: Configuration, implicit val ex: ExecutionContext) {
  val pagination: PaginationConfig = config.get[PaginationConfig]("daffodil.model.pagination")

  def create(title: String): Future[EventType] = dao.insert(title)
  def list(search: Option[String], page: Int = pagination.page, size: Int = pagination.size): Future[Seq[EventType]] = {
    dao.queries(search, page, size)
  }
  def count(search: Option[String]): Future[Int] = dao.count(search)
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
