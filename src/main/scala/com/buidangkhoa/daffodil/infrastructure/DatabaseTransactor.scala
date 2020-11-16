package com.buidangkhoa.daffodil.infrastructure

import cats.effect.{Blocker, ContextShift, IO, Resource}
import com.buidangkhoa.daffodil.config.DatabaseConfig
import doobie.hikari.HikariTransactor
import scala.concurrent.ExecutionContext
import org.flywaydb.core.Flyway

object DatabaseTransactor {
  def transactor(config: DatabaseConfig,
                 ce: ExecutionContext)(
    implicit cs: ContextShift[IO]
  ): Resource[IO, HikariTransactor[IO]] = {
    HikariTransactor.newHikariTransactor[IO](
      driverClassName = "com.mysql.jdbc.Driver",
      url = config.url,
      user = config.username,
      pass = config.password,
      ce,
      Blocker.liftExecutionContext(ce)
    )
  }

  def initialize(transactor: HikariTransactor[IO]): IO[Unit] = {
    transactor.configure { dataSource =>
      IO {
        val flyWay = Flyway.configure().dataSource(dataSource).load()
        flyWay.migrate()
        ()
      }
    }
  }
}
