package controllers.api

import javax.inject.Inject

import play.api.mvc._
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class EventTypeRouter @Inject() (controller: EventTypeController) extends SimpleRouter {
  override def routes: Routes = {
    case GET(p"/") => controller.index
  }
}
