package com.buidangkhoa.daffodil

import cats.effect.IO
import com.buidangkhoa.daffodil.common.ErrorHttpResponse
import com.buidangkhoa.daffodil.event_type.exceptions.{EventTypeNotFoundException, InvalidEventTypeTitleException}
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
        val eventTypeOption = for {
          eventTypeReq <- req.decodeJson[EventTypeRequest]
          eventTypeOption <- service.create(eventTypeReq.title)
        } yield eventTypeOption
        eventTypeOption.flatMap(evt => Ok(evt)).handleErrorWith({
          case InvalidEventTypeTitleException(ex) => BadRequest(
            ErrorHttpResponse(error = true, ex)
          )
        })
      case GET -> Root / "api" / "event-types" / IntVar(id) =>
        service.get(id).flatMap(evt => Ok(evt))
          .handleErrorWith({
            case EventTypeNotFoundException(ex) => NotFound(
              ErrorHttpResponse(error = true, ex)
            )
          })
      case req @ PUT -> Root / "api" / "event-types"/ IntVar(id) =>
        val eventTypeOption = for {
          eventTypeReq <- req.decodeJson[EventTypeRequest]
          eventTypeOption <- service.update(id, eventTypeReq.title)
        } yield eventTypeOption
        eventTypeOption.flatMap(evt => Ok(evt)).handleErrorWith({
          case InvalidEventTypeTitleException(ex) => BadRequest(
            ErrorHttpResponse(error = true, ex)
          )
        })
      case DELETE -> Root / "api" / "event-types" / IntVar(id) =>
        service.remove(id).flatMap(evt => Ok(evt))
          .handleErrorWith({
            case EventTypeNotFoundException(ex) => NotFound(
              ErrorHttpResponse(error = true, ex)
            )
          })
    }
  }
}
