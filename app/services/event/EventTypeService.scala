package services.event

import models.EventType
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class EventTypeService {
  def create(eventType: EventType): Future[EventType] = {
    Future {
      eventType
    }
  }
}
