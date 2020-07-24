package controllers.api

import javax.inject.Inject
import play.api.mvc._
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird.{?, _}

class EventTypeRouter @Inject() (controller: EventTypeController) extends SimpleRouter {
  override def routes: Routes = {
    case GET(p"/" ? q_o"p=${int(page)}"&q_o"s=${int(size)}"&q_o"q=${search}") =>
      controller.index(search, page.getOrElse(1), size.getOrElse(10))
    case POST(p"/") => controller.insert
    case PUT(p"/${int(id)}") => controller.updateOne(id)
    case GET(p"/${int(id)}") => controller.getOne(id)
    case DELETE(p"/${int(id)}" ? q_o"h=${bool(isHardRemoval)}") => controller.removeOne(id, isHardRemoval.getOrElse(false))
  }
}
