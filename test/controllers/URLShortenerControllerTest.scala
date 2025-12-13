package controllers

import org.scalatestplus.play.PlaySpec
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._

class URLShortenerControllerTest
  extends PlaySpec with Results {


  "URLShortenerController" should {

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
    val fixtureController = new URLShortenerController(stubControllerComponents())

    val notLongURL: String = "https://en.wikipedia.org/wiki/CAP_theorem"
    val longURL =
      "https://www.google.com/search?client=firefox-b-d&hs=MSd9&sca_esv=14371eb37312ee0c&q=long+url&source=lnms&fbs=AIIjpHxU7SXXniUZfeShr2fp4giZ1Y6MJ25_tmWITc7uy4KIeuYzzFkfneXafNx6OMdA4MQRJc_t_TQjwHYrzlkIauOK_IaFSQcTHs2AgJbmYqOLNlPDT0Hy19TTgd1LyYk-nATqUlksEoQZVG09gZfle_U4HWWnSbP00Bg1jXXhxPXHO_ZklXeHNi63K93FV-i7HL9XSIXF&sa=X&ved=2ahUKEwjHguermbqRAxXaBhAIHVqzEJcQ0pQJegQIChAB&biw=1728&bih=995&dpr=2"


    "shorten not so long URL" in {
      val controllerResponse = fixtureController.shortenURL.apply(FakeRequest(POST, "/").withTextBody(notLongURL))
      val shortenedURL = contentAsString(controllerResponse)
      info("shortened url = " + shortenedURL)
      shortenedURL must endWith("7743b6f")
      contentAsString(fixtureController.decodeShortURL(shortenedURL).apply(FakeRequest(GET, shortenedURL))) must be(notLongURL)
    }

    "decode shortened not so long URL" in {
      val shortened = contentAsString(fixtureController.shortenURL.apply(FakeRequest(POST, "/").withTextBody(notLongURL)))
      contentAsString(fixtureController.decodeShortURL(shortened).apply(FakeRequest(GET, shortened))) must be(notLongURL)
    }

    "shorten long URL" in {
      val controllerResponse = fixtureController.shortenURL.apply(FakeRequest(POST, "/").withTextBody(longURL))
      val shortenedURL = contentAsString(controllerResponse)
      info("shortened url = " + shortenedURL)
      shortenedURL must endWith("ccfdf9c")
    }

    "decode shortened long URL" in {
      val shortened = contentAsString(fixtureController.shortenURL.apply(FakeRequest(POST, "/").withTextBody(longURL)))
      contentAsString(fixtureController.decodeShortURL(shortened).apply(FakeRequest(GET, shortened))) must be(longURL)
    }
  }
}
