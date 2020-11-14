package com.buidangkhoa.daffodil.config

import cats.effect.{IO, Resource}
import io.circe.config.parser
import io.circe.generic.auto._

case class AppConfig(
                    serverConfig: ServerConfig,
                    databaseConfig: DatabaseConfig
                    )

object AppConfig {
  def load(): Resource[IO, AppConfig] = {
    val config = for {
      dbConf <- parser.decodePathF[IO, DatabaseConfig]("database")
      serverConf <- parser.decodePathF[IO, ServerConfig]("server")
    } yield AppConfig(serverConf, dbConf)
    Resource.liftF(config)
  }
}
