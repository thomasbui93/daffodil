package controllers.api

import configuration.PaginationConfig
import javax.inject.Inject
import play.api.Configuration
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird.{?, _}

class EventTypeRouter @Inject() (controller: EventTypeController, config: Configuration) extends SimpleRouter {
  val pagination: PaginationConfig = config.get[PaginationConfig]("daffodil.model.pagination")
  override def routes: Routes = {
    case GET(p"/" ? q_o"p=${int(page)}"&q_o"s=${int(size)}"&q_o"q=${search}") =>
      controller.index(search, page.getOrElse(pagination.page), size.getOrElse(pagination.size))
    case POST(p"/") => controller.insert
    case PUT(p"/${int(id)}") => controller.updateOne(id)
    case GET(p"/${int(id)}") => controller.getOne(id)
    case DELETE(p"/${int(id)}" ? q_o"h=${bool(isHardRemoval)}") => controller.removeOne(id, isHardRemoval.getOrElse(false))
  }
}
