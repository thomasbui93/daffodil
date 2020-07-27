package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "HomeController GET" should {
    "render the index page from a new instance of controller" in {
      val controller = new HomeController(stubControllerComponents())
      val homeMessage = controller.index().apply(FakeRequest(GET, "/"))
      val json = contentAsJson(homeMessage)
      val message = (json \ "message").as[String]

      status(homeMessage) mustBe OK
      contentType(homeMessage) mustBe Some("application/json")
      message mustBe "Welcome to Daffodil application."
    }
  }
}
