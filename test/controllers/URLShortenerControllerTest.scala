package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatestplus.play.PlaySpec
import org.specs2.mutable.BeforeAfter
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global

class URLShortenerControllerTest extends PlaySpec with Results with BeforeAfter {

  val beforeURL: String = "https://en.wikipedia.org/wiki/CAP_theorem"

  "URLShortenerControllerTest" should {

    implicit val actorSystem = ActorSystem("test")
    val controller = new URLShortenerController(stubControllerComponents(), actorSystem)
    implicit val ec = actorSystem.dispatcher

    var encoded = ""

    "shorten" in {
      implicit val materializer = ActorMaterializer()
      try {
        var resultFuture = controller.shorten.apply(FakeRequest().withBody(beforeURL).withMethod(POST))
        encoded = contentAsString(resultFuture)
        encoded mustNot be("")
      } finally {
        actorSystem.terminate()
      }
    }

    "decode" in {
      val resultFuture = controller.decode(encoded).apply(FakeRequest())
      val res = contentAsString(resultFuture)
      res must equal(beforeURL)
    }
  }

  override def before: Any = {

  }

}
