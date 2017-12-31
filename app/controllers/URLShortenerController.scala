package controllers

import java.util
import java.util.Random
import javax.inject._

import akka.actor.ActorSystem
import io.swagger.annotations.{Api, ApiOperation}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
  * This is just initial
  */
@Singleton
@Api(value = "/shorten", description = "URL shortener")
class URLShortenerController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext)
  extends AbstractController(cc) {

  val urls: util.Map[String, String] = new util.HashMap[String, String]

  val baseURL: String = "http://localhost/"

  /**
    */
  @ApiOperation(value = "Shorten url in the POST body.",
    notes = "",
    httpMethod = "POST")
  def shorten = Action.async {
    implicit request =>
      getFutureShortenedURL(request.body.asText.getOrElse("")).map { msg => Ok(msg) }
  }

  private def getFutureShortenedURL(longUrl: String): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    val r: Random = new Random
    val shortened: String = baseURL + longUrl.hashCode
    urls.put(shortened, longUrl)
    promise.success(shortened)
    promise.future
  }

  @ApiOperation(value = "Return long URL",
    notes = "",
    httpMethod = "GET")
  def decode(url: String) = Action.async {
    getFutureDecode(url).map { msg =>
      msg match {
        case "404" => NotFound(msg)
        case _ => Ok(msg)
      }
    }
  }

  private def getFutureDecode(url: String): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    promise.success(
      urls.getOrDefault(url, "404")
    )
    promise.future
  }
}
