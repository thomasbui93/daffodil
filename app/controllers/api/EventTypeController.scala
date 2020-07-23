package controllers.api

import javax.inject.Inject
import play.api.mvc._

class EventTypeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action {
    Ok("It works!")
  }
}
