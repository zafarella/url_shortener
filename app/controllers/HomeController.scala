package controllers

import javax.inject._

import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index: Action[AnyContent] = Action {
    Ok(views.html.index("Let's shorten some urls."))
  }

  def redirectToSwagger: Action[AnyContent] = Action.async {
    implicit request =>
      Future(
        Redirect(url = "/assets/lib/swagger-ui/index.html?", queryString = Map("url" -> Seq("/v1/api-json")))
      )
  }

}
