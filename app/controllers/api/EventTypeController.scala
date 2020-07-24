package controllers.api

import javax.inject.Inject
import play.api.mvc._
import play.api.libs.json._
import services.event.EventTypeService
import scala.concurrent.{ExecutionContext, Future}
import models.EventType

case class SearchResult[A](list: Seq[A], count: Int)

class EventTypeController @Inject()(implicit val ec: ExecutionContext,
                                     val eventTypeService: EventTypeService,
                                     val cc: ControllerComponents)
  extends AbstractController(cc) {

  def index(search: Option[String], page: Int, size: Int): Action[AnyContent] = Action.async { implicit request =>
    val result = for {
      list <- eventTypeService.list(search, page, size)
      count <- eventTypeService.count(search, page, size)
    } yield SearchResult[EventType](list, count)
    result.map(data => Ok(Json.obj(
      "list" -> Json.toJson(data.list),
      "count" -> Json.toJson(data.count)
    ))).recover {
      case e: Exception => InternalServerError("You failed!")
    }
  }

  def insert: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val eventTypeResult = request.body.validate[EventType]
    eventTypeResult.fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      eventTypeJson => {
        eventTypeService.create(eventTypeJson.title)
          .map(eventType => Ok(Json.toJson(eventType)))
          .recover {
            case e: Exception => InternalServerError("You failed!")
          }
      }
    )
  }

  def getOne(id: Int): Action[AnyContent] = Action.async {
    eventTypeService.read(id)
      .map({
        case Some(eventType: EventType) => Ok(Json.toJson(eventType))
        case None => NotFound(Json.obj("message" -> "Event type not found."))
      })
  }

  def removeOne(id: Int, isHardRemoval: Boolean): Action[AnyContent] = Action.async {
    eventTypeService
      .remove(id, isHardRemoval)
      .map { _.fold(
        error => BadRequest(Json.obj("message" -> error)),
        removedId => Ok(Json.obj("message" -> s"Successfully remove item with $removedId"))
      )}
  }

  def updateOne(id: Int): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val eventTypeResult = request.body.validate[EventType]
    eventTypeResult.fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      eventTypeJson => {
        eventTypeService.update(id, eventTypeJson)
          .map(eventType => {
            println(eventType)
            Ok(Json.toJson(eventType))
          })
          .recover {
            case e: Exception => InternalServerError("You failed!")
          }
      }
    )
  }
}
