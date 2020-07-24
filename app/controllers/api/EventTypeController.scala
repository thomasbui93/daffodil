package controllers.api

import javax.inject.Inject
import play.api.mvc._
import play.api.libs.json._
import services.event.EventTypeService
import scala.concurrent.{ExecutionContext, Future}
import models.EventType

class EventTypeController @Inject()(implicit val ec: ExecutionContext,
                                     val eventTypeService: EventTypeService,
                                     val cc: ControllerComponents)
  extends AbstractController(cc) {

  def index(search: Option[String], page: Int, size: Int): Action[AnyContent] = Action.async { implicit request =>
    eventTypeService.list(search, page, size)
      .map(eventTypeList => Ok(Json.toJson(eventTypeList)))
      .recover {
        case e: Exception => InternalServerError("You failed!")
      }
  }

  def insert: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val eventTypeResult = request.body.validate[EventType]
    eventTypeResult.fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      place => {
        eventTypeService.create(place.title)
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

  def removeOne(id: Int): Action[AnyContent] = Action.async {
    eventTypeService
      .remove(id)
      .map { _.fold(
        error => BadRequest(Json.obj("message" -> error)),
        removedId => Ok(Json.obj("message" -> s"Successfully remove item with $removedId"))
      )}
  }
}
