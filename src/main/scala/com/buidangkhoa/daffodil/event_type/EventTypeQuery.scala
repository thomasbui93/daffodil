package com.buidangkhoa.daffodil.event_type

import doobie.util.transactor.Transactor
import cats.effect.IO
import doobie.implicits._

object EventTypeQuery {
  def insert(title: String, tx: Transactor[IO]): IO[Int] = {
    sql"""INSERT INTO event_types (title) VALUES ($title)"""
      .update
      .run
      .transact(tx)
  }

  def queryOne(id: Int, tx: Transactor[IO]): IO[Option[EventType]] = {
    sql"""SELECT id, title, createdAt, updatedAt FROM event_types WHERE id = $id"""
      .query[EventType]
      .option
      .transact(tx)
  }

  def query(tx: Transactor[IO]): IO[Seq[EventType]] = {
    sql"""SELECT id, title, createdAt, updatedAt FROM event_types"""
      .query[EventType]
      .to[List]
      .transact(tx)
  }

  def count(tx: Transactor[IO]): IO[Int] = {
    sql"""SELECT COUNT(*) FROM event_types"""
      .query[Int]
      .unique
      .transact(tx)
  }
}
