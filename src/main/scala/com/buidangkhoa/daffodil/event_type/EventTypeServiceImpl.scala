package com.buidangkhoa.daffodil.event_type

import doobie.util.transactor.Transactor
import cats.effect.IO
import com.buidangkhoa.daffodil.event_type.exceptions.{EventTypeNotFoundException, InvalidEventTypeTitleException}

class EventTypeServiceImpl(tx: Transactor[IO]) extends EventTypeService[IO] {
  def checkEmptyTitle(title: String): IO[Unit] = {
    if (title.length == 0) {
      IO.raiseError(InvalidEventTypeTitleException("Event type's title can not be empty."))
    } else {
      IO(())
    }
  }
  def checkTitleExisted(title: String): IO[Unit] = {
    EventTypeQuery
      .findByTitle(title, tx)
      .flatMap(result => {
        if (result >= 1) {
          IO.raiseError(InvalidEventTypeTitleException("Event type's title already existed."))
        } else {
          IO(())
        }
      })
  }
  def validateTitle(title: String): IO[Unit] = {
    for {
      _ <- checkEmptyTitle(title)
      _ <- checkTitleExisted(title)
    } yield IO(())
  }
  override def create(title: String): IO[EventType] = {
    for {
      _ <- validateTitle(title)
      evt <- EventTypeQuery.insert(title, tx)
    } yield evt
  }
  override def get(id: Int): IO[EventType] = {
    EventTypeQuery.queryOne(id, tx)
      .flatMap {
        case Some(eventType) => IO(eventType)
        case None => IO.raiseError(EventTypeNotFoundException(s"""Event type not found with id ${id}"""))
      }
  }
  override def list(): IO[Seq[EventType]] = EventTypeQuery.query(tx)
  override def count(): IO[Int] = EventTypeQuery.count(tx)
  override def remove(id: Int): IO[Unit] = {
    EventTypeQuery.remove(id, tx)
      .flatMap { rows =>
        if (rows == 0) {
          IO.raiseError(EventTypeNotFoundException(s"""Event type not found with id ${id}"""))
        } else {
          IO(())
        }
      }
  }
  override def update(id: Int, title: String): IO[EventType] = {
    for {
      _ <- validateTitle(title)
      rows <- EventTypeQuery.update(id, title, tx)
      evt <- if (rows >= 1) {
        EventTypeQuery.queryOneAfterUpdate(id, tx)
      } else {
        IO.raiseError(EventTypeNotFoundException(s"""Event type not found with id ${id}"""))
      }
    } yield evt
  }
}
