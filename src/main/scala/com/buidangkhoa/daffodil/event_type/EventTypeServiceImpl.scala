package com.buidangkhoa.daffodil.event_type

import doobie.util.transactor.Transactor
import cats.effect.IO
import com.buidangkhoa.daffodil.event_type.exceptions.{EventTypeCreationFailedException, EventTypeNotFoundException, EventTypeUpdateException}

class EventTypeServiceImpl(tx: Transactor[IO]) extends EventTypeService[IO] {
  def isTitleExisted(title: String): IO[Boolean] = {
    EventTypeQuery
      .findByTitle(title, tx)
      .map(result => result >= 1)
  }
  override def create(title: String): IO[Either[EventTypeCreationFailedException, EventType]] = {
    isTitleExisted(title).flatMap(isValid => {
      if (isValid) {
        IO(Left(EventTypeCreationFailedException("Event type's title is duplicated.")))
      } else {
        val evt = for {
          id <- EventTypeQuery.insert(title, tx)
          evt <- EventTypeQuery.queryOneAfterCreate(id, tx)
        } yield evt
        evt.map(eventType => Right(eventType))
      }
    })
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

  override def remove(id: Int): IO[Boolean] = {
    EventTypeQuery.remove(id, tx)
      .map(affectedRows => affectedRows >= 1)
  }

  override def update(id: Int, title: String): IO[Either[EventTypeUpdateException, EventType]] = {
    isTitleExisted(title).flatMap(isValid => {
      if (isValid) {
        IO(Left(EventTypeUpdateException("Event type's title is duplicated.")))
      } else {
        EventTypeQuery.update(id, title, tx)
          .flatMap(rows => {
            if (rows >= 1) {
              EventTypeQuery.queryOneAfterCreate(id, tx).map(ev => Right(ev))
            } else {
              IO(Left(EventTypeUpdateException(s"""Event type not found with ${id}""")))
            }
          })
      }
    })
  }
}
