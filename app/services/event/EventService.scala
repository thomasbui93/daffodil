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
}
