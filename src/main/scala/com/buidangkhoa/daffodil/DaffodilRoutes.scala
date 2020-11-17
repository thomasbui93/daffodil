package com.buidangkhoa.daffodil

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import health_check.HealthCheckService
import org.http4s.circe._
import event_type.{EventTypeListResponse, EventTypeRequest, EventTypeService}

object DaffodilRoutes {
  def healthCheckRoutes(healthCheckService: HealthCheckService[IO]): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "z" / "ping" =>
        for {
          healthCheck <- healthCheckService.healthCheck()
          resp <- Ok(healthCheck)
        } yield resp
    }
  }

  def eventTypesRoutes(service: EventTypeService[IO]): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "api" / "event-types" =>
        for {
          eventTypeList <- service.list()
          count <- service.count()
          resp <- Ok(EventTypeListResponse(eventTypeList, count))
        } yield resp
      case req @ POST -> Root / "api" / "event-types" =>
        for {
          eventTypeReq <- req.decodeJson[EventTypeRequest]
          eventTypeOption <- service.create(eventTypeReq.title)
          resp <- eventTypeOption.fold(
            ex => BadRequest(ex.message),
            eventType => Ok(eventType)
          )
        } yield resp
      case GET -> Root / "api" / "event-types" / IntVar(id) =>
        for {
          eventTypeOption <- service.get(id)
          resp <- eventTypeOption.fold(
            _ => NotFound("Event type is not found."),
            eventType => Ok(eventType)
          )
        } yield resp
      case req @ PUT -> Root / "api" / "event-types"/ IntVar(id) =>
        for {
          eventTypeReq <- req.decodeJson[EventTypeRequest]
          eventTypeOption <- service.update(id, eventTypeReq.title)
          resp <- eventTypeOption.fold(
            _ => NotFound("Event type is not found."),
            eventType => Ok(eventType)
          )
        } yield resp
      case DELETE -> Root / "api" / "event-types" / IntVar(id) =>
        for {
          removedItem <- service.remove(id)
          resp <- if (removedItem) Ok() else NotFound("Event type is not found.")
        } yield resp
    }
  }
}
