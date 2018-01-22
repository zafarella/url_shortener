import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._

class FunctionalTest extends PlaySpec with GuiceOneAppPerSuite {
  "Routes" should {

    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(BAD_REQUEST)
    }

    "send 200 on a good request" in {
      route(app, FakeRequest(GET, "/")).map(status(_)) mustBe Some(OK)
    }
  }
}
