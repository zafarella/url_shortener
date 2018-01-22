package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatestplus.play.PlaySpec
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global

class URLShortenerControllerTest extends PlaySpec with Results {

  val beforeURL: String = "https://en.wikipedia.org/wiki/CAP_theorem"

  "URLShortenerControllerTest" should {

    implicit val actorSystem = ActorSystem("test")
    val controller = new URLShortenerController(stubControllerComponents(), actorSystem)
    implicit val ec = actorSystem.dispatcher

    var encoded = ""

    "shorten" in {
      implicit val materializer = ActorMaterializer()
      try {
        var resultFuture = controller.shorten.apply(FakeRequest(POST, "/").withTextBody(beforeURL))
        encoded = contentAsString(resultFuture)
        System.out.println(encoded)
        encoded must endWith("7743B6F")
      } finally {
        actorSystem.terminate()
      }
    }

    "decode shortened" in {
      // FIXME
      //      val shortened = contentAsString(controller.shorten.apply(FakeRequest(POST, "/").withTextBody(beforeURL)))
      //      contentAsString(controller.decode(shortened).apply(FakeRequest(GET, shortened))) must be(beforeURL)
    }
  }
}
