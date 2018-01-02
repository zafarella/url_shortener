package controllers

import java.security.MessageDigest
import java.util
import javax.inject._
import javax.xml.bind.DatatypeConverter

import akka.actor.ActorSystem
import io.swagger.annotations.{Api, ApiOperation}
import play.Logger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
  * This is just initial
  */
@Singleton
@Api(value = "/")
class URLShortenerController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext)
  extends AbstractController(cc) {

  val urls: util.Map[String, String] = new util.HashMap[String, String]
  val baseURL: String = "http://localhost:9000/"

  /**
    */
  @ApiOperation(value = "Shorten url in the POST body.", notes = "", httpMethod = "POST")
  def shorten = Action.async {
    implicit request =>
      val url = request.body.asText.getOrElse("")
      if (!url.isEmpty)
        getFutureShortenedURL(url).map { msg => Ok(msg) }
      else
        Future {
          BadRequest
        }
  }

  @ApiOperation(value = "Return long URL", notes = "", httpMethod = "GET")
  def decode(url: String) = Action.async {
    getFutureDecode(url).map { url =>
      url match {
        case "404" => NotFound(url)
        case _ => Ok(url)
      }
    }
  }

  def getFutureShortenedURL(longUrl: String): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    val msdDigest = MessageDigest.getInstance("SHA-1")
    msdDigest.update(longUrl.getBytes("UTF-8"), 0, longUrl.length)
    val shortHash = (DatatypeConverter.printHexBinary(msdDigest.digest)).substring(0, 7)
    val shortened: String = baseURL + shortHash
    urls.put(shortHash, longUrl)
    Logger.debug("Long url = " + longUrl)
    Logger.debug("Shortened = " + shortened)

    promise.success(shortened)
    promise.future
  }

  def getFutureDecode(url: String): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    Logger.debug("URL for decode = " + url)
    Logger.debug("Decoded long url = " + urls.getOrDefault(url, "404"))

    promise.success(urls.getOrDefault(url, "404"))
    promise.future
  }
}
