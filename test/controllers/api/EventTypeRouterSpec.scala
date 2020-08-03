package controllers.api

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.{RequestHeader, Result}
import play.api.test._
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._
import models.EventType
import scala.concurrent.Future

class EventTypeRouterSpec extends PlaySpec with GuiceOneAppPerTest {
  "EventTypeController API" should {
    "EventTypeController API POST" should {
      "should be 200 status if payload is valid" in {
        val title = "Sample"
        val request = FakeRequest(POST, "/api/event-types")
          .withHeaders(HOST -> "localhost:9000", "Content-type" -> "application/json")
          .withCSRFToken
          .withBody(s"""{"title": "${title}"}""")
        val result: Future[Result] = route(app, request).get
        val posts = contentAsJson(result)
        val eventType = posts.as[EventType]
        status(result) mustBe OK
        eventType.title mustBe title
        eventType.id mustBe defined
      }

      "should be 401 status if payload is invalid" in {
        val title = "Sample"
        val request = FakeRequest(POST, "/api/event-types")
          .withHeaders(HOST -> "localhost:9000", "Content-type" -> "application/json")
          .withCSRFToken
          .withBody(s"""{"sample": "${title}"}""")
        val result: Future[Result] = route(app, request).get
        status(result) mustBe BAD_REQUEST
      }
    }
    "EventTypeController API GET" should {
      "should be 200 status" in {
        val request = FakeRequest(GET,"/api/event-types")
          .withHeaders(HOST -> "localhost:9000")
          .withCSRFToken
        val result: Future[Result] = route(app, request).get
        val json = contentAsJson(result)
        val eventTypes = (json \ "list").as[Seq[EventType]]
        val count = (json \ "count").as[Int]

        eventTypes.length mustBe 1
        count mustBe 1
        status(result) mustBe OK
      }
    }

    "EventTypeController API GET single record" should {
      "should be 200 status with valid id" in {
        val request = FakeRequest(GET,"/api/event-types/1")
          .withHeaders(HOST -> "localhost:9000")
          .withCSRFToken
        val result: Future[Result] = route(app, request).get
        val json = contentAsJson(result)
        val eventType = json.as[EventType]

        eventType.id mustBe Some(1)
      }

      "should be 404 status with unknown id" in {
        val request = FakeRequest(GET,"/api/event-types/1000")
          .withHeaders(HOST -> "localhost:9000")
          .withCSRFToken
        val result: Future[Result] = route(app, request).get
        status(result) mustBe NOT_FOUND
      }

      "should be 401 status with invalid id" in {
        val request = FakeRequest(GET,"/api/event-types/xxx")
          .withHeaders(HOST -> "localhost:9000")
          .withCSRFToken
        val result: Future[Result] = route(app, request).get
        status(result) mustBe NOT_FOUND
      }
    }

    "EventTypeController API PUT" should {
      "should be 200 status when valid payload is received" in {
        val title = "new title"
        val request = FakeRequest(PUT,"/api/event-types/1")
          .withHeaders(HOST -> "localhost:9000", "Content-type" -> "application/json")
          .withCSRFToken
          .withBody(s"""{"title": "${title}"}""")
        val result: Future[Result] = route(app, request).get
        val json = contentAsJson(result)
        val eventType = json.as[EventType]

        eventType.title mustBe title
        status(result) mustBe OK
      }

      "should be 400 status when invalid payload is received" in {
        val request = FakeRequest(PUT,"/api/event-types/1")
          .withHeaders(HOST -> "localhost:9000", "Content-type" -> "application/json")
          .withCSRFToken
          .withBody(s"""{"sample": "xxx"}""")
        val result: Future[Result] = route(app, request).get
        status(result) mustBe BAD_REQUEST
      }

      "should be 404 status when id is unknown" in {
        val request = FakeRequest(PUT,"/api/event-types/100")
          .withHeaders(HOST -> "localhost:9000", "Content-type" -> "application/json")
          .withCSRFToken
          .withBody(s"""{"sample": "xxx"}""")
        val result: Future[Result] = route(app, request).get
        status(result) mustBe BAD_REQUEST
      }

      "should be 400 status when id is invalid" in {
        val request = FakeRequest(PUT,"/api/event-types/xxx")
          .withHeaders(HOST -> "localhost:9000", "Content-type" -> "application/json")
          .withCSRFToken
          .withBody(s"""{"sample": "xxx"}""")
        val result: Future[Result] = route(app, request).get
        status(result) mustBe NOT_FOUND
      }
    }
  }
  "EventTypeController API DELETE" should {
    "should be 200 status when valid id is received" in {
      val request = FakeRequest(DELETE, "/api/event-types/1")
        .withHeaders(HOST -> "localhost:9000")
        .withCSRFToken
      val result: Future[Result] = route(app, request).get
      status(result) mustBe OK
    }

    "should be 404 status when invalid id is received" in {
      val request = FakeRequest(DELETE, "/api/event-types/xxx")
        .withHeaders(HOST -> "localhost:9000")
        .withCSRFToken
      val result: Future[Result] = route(app, request).get
      status(result) mustBe NOT_FOUND
    }
  }
}
