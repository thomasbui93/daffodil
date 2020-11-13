package com.buidangkhoa.daffodil.config

import cats.effect.IO
import io.circe.config.parser
import io.circe.generic.auto._

case class AppConfig(
                    serverConfig: ServerConfig,
                    databaseConfig: DatabaseConfig
                    )

object AppConfig {
  def load(): IO[AppConfig] = {
    for {
      dbConf <- parser.decodePathF[IO, DatabaseConfig]("database")
      serverConf <- parser.decodePathF[IO, ServerConfig]("server")
    } yield AppConfig(serverConf, dbConf)
  }
}
