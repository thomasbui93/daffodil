package com.buidangkhoa.daffodil

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import com.buidangkhoa.daffodil.config.AppConfig
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object DaffodilServer {

  def stream[F[_]: ConcurrentEffect](appConfig: AppConfig)(implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    for {
      client <- BlazeClientBuilder[F](global).stream
      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg = Jokes.impl[F](client)

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
        DaffodilRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        DaffodilRoutes.jokeRoutes[F](jokeAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(appConfig.serverConfig.port, appConfig.serverConfig.host)
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
