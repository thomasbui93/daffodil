package com.buidangkhoa.daffodil.event_type

import com.buidangkhoa.daffodil.event_type.exceptions.{EventTypeCreationFailedException, EventTypeNotFoundException, EventTypeUpdateException}

trait EventTypeService[F[_]] {
  def create(title: String): F[Either[EventTypeCreationFailedException, EventType]]
  def update(id: Int, title: String): F[Either[EventTypeUpdateException, EventType]]
  def remove(id: Int): F[Boolean]
  def get(id: Int): F[Either[EventTypeNotFoundException, EventType]]
  def list(): F[Seq[EventType]]
  def count(): F[Int]
}
