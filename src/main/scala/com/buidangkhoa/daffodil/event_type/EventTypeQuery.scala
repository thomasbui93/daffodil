package com.buidangkhoa.daffodil.event_type

import doobie.util.transactor.Transactor
import cats.effect.IO
import doobie.implicits._
import com.typesafe.scalalogging.LazyLogging
import doobie.util.log.LogHandler

object EventTypeQuery extends LazyLogging {
  def insert(title: String, tx: Transactor[IO]): IO[EventType] = {
    val evt = for {
      id <- sql"""INSERT INTO event_types (title) VALUES ($title)"""
        .update
        .withUniqueGeneratedKeys[Int]("id")
      evt <- sql"""SELECT id, title, createdAt, updatedAt FROM event_types WHERE id = $id"""
        .query[EventType]
        .unique
    } yield evt
    evt.transact(tx)
  }

  def findByTitle(title: String, tx: Transactor[IO]): IO[Int] = {
    sql"""SELECT COUNT(*) FROM event_types WHERE title = $title"""
      .query[Int]
      .unique
      .transact(tx)
  }

  def queryOne(id: Int, tx: Transactor[IO]): IO[Option[EventType]] = {
    sql"""SELECT id, title, createdAt, updatedAt FROM event_types WHERE id = $id"""
      .query[EventType]
      .option
      .transact(tx)
  }

  def queryOneAfterUpdate(id: Int, tx: Transactor[IO]): IO[EventType] = {
    sql"""SELECT id, title, createdAt, updatedAt FROM event_types WHERE id = $id"""
      .query[EventType]
      .unique
      .transact(tx)
  }

  def query(limit: Int, offset: Int, tx: Transactor[IO]): IO[Seq[EventType]] = {
    sql"""SELECT id, title, createdAt, updatedAt FROM event_types LIMIT $limit OFFSET $offset"""
      .queryWithLogHandler[EventType](LogHandler.jdkLogHandler)
      .to[List]
      .transact(tx)
  }

  def count(tx: Transactor[IO]): IO[Int] = {
    sql"""SELECT COUNT(*) FROM event_types"""
      .query[Int]
      .unique
      .transact(tx)
  }

  def remove(id: Int, tx: Transactor[IO]): IO[Int] = {
    sql"""DELETE FROM event_types WHERE id = $id"""
      .update
      .run
      .transact(tx)
  }

  def update(id: Int, title: String, tx: Transactor[IO]): IO[Int] = {
    sql"""UPDATE event_types SET title = $title WHERE id = $id"""
      .update
      .run
      .transact(tx)
  }
}
