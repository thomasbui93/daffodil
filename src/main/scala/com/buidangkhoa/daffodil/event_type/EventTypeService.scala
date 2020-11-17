package com.buidangkhoa.daffodil.event_type

import com.buidangkhoa.daffodil.event_type.exceptions.EventTypeNotFoundException

trait EventTypeService[F[_]] {
  def create(title: String): F[EventType]
  //def update(title: String): F[EventType]
  //def remove(id: Int): F[Boolean]
  def get(id: Int): F[Either[EventTypeNotFoundException, EventType]]
  def list(): F[Seq[EventType]]
  def count(): F[Int]
}
