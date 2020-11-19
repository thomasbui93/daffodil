package com.buidangkhoa.daffodil.event_type

trait EventTypeService[F[_]] {
  def create(title: String): F[EventType]
  def update(id: Int, title: String): F[EventType]
  def remove(id: Int): F[Unit]
  def get(id: Int): F[EventType]
  def list(): F[Seq[EventType]]
  def count(): F[Int]
}
