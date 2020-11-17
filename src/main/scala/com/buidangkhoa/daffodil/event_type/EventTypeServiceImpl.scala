package com.buidangkhoa.daffodil.event_type

import doobie.util.transactor.Transactor
import cats.effect.IO
import com.buidangkhoa.daffodil.event_type.exceptions.EventTypeNotFoundException

class EventTypeServiceImpl(tx: Transactor[IO]) extends EventTypeService[IO] {
  override def create(title: String): IO[EventType] = {
    for {
      id <- EventTypeQuery.insert(title, tx)
      eventTypeEither <- get(id)
      eventType = eventTypeEither.fold(
        _ => EventType(id, title, "", ""),
        identity[EventType])
    } yield eventType
  }

  override def get(id: Int): IO[Either[EventTypeNotFoundException, EventType]] = {
    EventTypeQuery.queryOne(id, tx)
      .map {
        case Some(eventType) => Right(eventType)
        case None => Left(EventTypeNotFoundException(s"""Event type not found with ${id}"""))
      }
  }

  override def list(): IO[Seq[EventType]] = EventTypeQuery.query(tx)

  override def count(): IO[Int] = EventTypeQuery.count(tx)
}
