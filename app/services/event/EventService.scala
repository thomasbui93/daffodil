package services.event

import configuration.PaginationConfig
import dao.event.{EventDAO, EventPayload, EventTypeDAO}
import models.{Event, EventType}
import javax.inject.Inject
import play.api.Configuration

import scala.concurrent.{ExecutionContext, Future}

class EventService @Inject()(eventDao: EventDAO, eventTypeDao: EventTypeDAO, config: Configuration, implicit val ex: ExecutionContext) {
  val p: PaginationConfig = config.get[PaginationConfig]("daffodil.model.pagination")

  def create(payload: EventPayload, typeId: Int): Future[Event] = {
    val eventType = eventTypeDao.getOne(typeId)
    eventType.flatMap {
      case Some(value) => eventDao.insert(payload, value)
      case None => Future.failed(new Exception("Event type not found."))
    }
  }
}
