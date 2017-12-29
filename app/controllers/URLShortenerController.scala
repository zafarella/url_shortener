package controllers

import java.util
import java.util.Random
import javax.inject._

import akka.actor.ActorSystem
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
  * This is just initial
  */
@Singleton
class URLShortenerController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext)
  extends AbstractController(cc) {

  val urls: util.Map[String, String] = new util.HashMap[String, String]

  val baseURL: String = "http://localhost/"

  /**
    */
  def shorten(longURL: String) = Action.async {
    getFutureShortenedURL(longURL).map { msg => Ok(msg) }
  }

  private def getFutureShortenedURL(longUrl: String): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    val r: Random = new Random
    val shortened: String = baseURL + longUrl.hashCode + String.valueOf(r.nextInt(1000))
    urls.put(shortened, longUrl)
    promise.success(shortened)
    promise.future
  }


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
