package com.buidangkhoa.daffodil.event_type

import com.buidangkhoa.daffodil.common.HttpConfiguration

trait EventTypeService[F[_]] {
  def create(title: String): F[EventType]
  def update(id: Int, title: String): F[EventType]
  def remove(id: Int): F[Unit]
  def get(id: Int): F[EventType]
  def list(page: Int = HttpConfiguration.paginationPage,
           size: Int = HttpConfiguration.paginationSize): F[Seq[EventType]]
  def count(): F[Int]
}
