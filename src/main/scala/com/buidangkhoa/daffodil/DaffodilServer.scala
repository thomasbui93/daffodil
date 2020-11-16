package com.buidangkhoa.daffodil

import cats.effect.{ContextShift, ExitCode, IO, Resource, Timer}
import com.buidangkhoa.daffodil.config.AppConfig
import com.buidangkhoa.daffodil.health_check.{HealthCheckResult, HealthCheckService, HealthCheckServiceImpl}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global
import infrastructure.DatabaseTransactor

object DaffodilServer {
  def serve()(implicit timer: Timer[IO], cx: ContextShift[IO]): IO[ExitCode] = {
    config().use(run)
  }
  def config()(implicit cx: ContextShift[IO]): Resource[IO, RootConfig] = {
    for {
      config <- AppConfig.load()
      ce <- ExecutionContexts.fixedThreadPool[IO](config.databaseConfig.poolSize)
      tx <- DatabaseTransactor.transactor(config.databaseConfig, ce)
    } yield RootConfig(tx, config)
  }
  private def run(rootConfig: RootConfig)(implicit timer: Timer[IO], cx: ContextShift[IO]): IO[ExitCode] = {
    for {
      _ <- DatabaseTransactor.initialize(rootConfig.transactor)
      httpApp = DaffodilRoutes.healthCheckRoutes(
        new HealthCheckServiceImpl(rootConfig.transactor)
      ).orNotFound
      finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
      exitCode <- BlazeServerBuilder[IO](global)
        .bindHttp(rootConfig.config.serverConfig.port, rootConfig.config.serverConfig.host)
        .withHttpApp(finalHttpApp)
        .serve
        .compile
        .lastOrError
    } yield exitCode
  }
  case class RootConfig(transactor: HikariTransactor[IO], config: AppConfig)
}
