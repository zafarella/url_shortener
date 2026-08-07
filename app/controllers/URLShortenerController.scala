package controllers

import io.github.bluuewhale.hashsmith.SwissMap

import java.net.{InetAddress, MalformedURLException, URL}
import java.security.MessageDigest
import java.util
import javax.inject._

//import javax.xml.bind.DatatypeConverter
import org.apache.commons.codec.binary.Hex

import play.Logger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * This is just initial
 */
@Singleton
//@Api("URL Shortener")
class URLShortenerController @Inject()(cc: ControllerComponents)
                                      (implicit exec: ExecutionContext)
  extends AbstractController(cc) {

  val urls: SwissMap[String, String] = new SwissMap[String, String]
  val baseURL: String = s"http://${InetAddress.getLocalHost.getHostName}:9000/"

  //  @ApiOperation(value = "Shorten URL in the POST body.",
  //    notes = "",
  //    httpMethod = "POST",
  //    response = classOf[String],
  //    consumes = "text/plain")
  //  @ApiImplicitParams(Array(
  //    new ApiImplicitParam(
  //      name = "Long URL",
  //      required = true,
  //      paramType = "body",
  //      dataType = "String",
  //      defaultValue = "http://www.wikipedia.org",
  //      example = "http://www.wikipedia.org",
  //      value = "http://www.wikipedia.org"
  //    )
  //
  //  ))
  //  @ApiResponses(Array(
  //    new ApiResponse(code = 404, message = "Not found"),
  //    new ApiResponse(code = 405, message = "Validation exception"))
  //  )
  def shortenURL: Action[AnyContent] = Action.async {
    implicit request =>
      try {
        val url = request.body.asText.getOrElse("")
        new URL(url)
        shortenURL(url).map {
          msg => Ok(msg)
        }
      }
      catch {
        case invalidURL: MalformedURLException =>
          Future {
            BadRequest
          }
      }
  }

  //  @ApiOperation(value = "Return long URL", notes = "", httpMethod = "GET")
  //  @ApiResponses(Array(
  //    new ApiResponse(code = 400, message = "Invalid URL supplied"),
  //    new ApiResponse(code = 405, message = "Validation exception"))
  //  )
  def decodeShortURL(
                      //              @ApiParam(value = "Short URL", required = true)
                      shortURL: String): Action[AnyContent] = Action.async {
    Logger.debug("URL for decode = " + shortURL)
    Logger.debug("Decoded long url = " + urls.getOrDefault(shortURL, "404"))

    Future {
      urls.getOrDefault(shortURL, "404") match {
        case "404" => NotFound(shortURL)
        case res => Ok(res)
      }
    }
  }

  private def shortenURL(longUrl: String): Future[String] = {

    val promise: Promise[String] = Promise[String]()
    val msdDigest = MessageDigest.getInstance("SHA-1")
    msdDigest.update(longUrl.getBytes("UTF-8"), 0, longUrl.length)

    val shortened: String =

      //      baseURL + DatatypeConverter.printHexBinary(msdDigest.digest).substring(0, 7)
      baseURL + Hex.encodeHexString(msdDigest.digest).substring(0, 7)

    urls.put(shortened, longUrl)

    Logger.debug("Long url = " + longUrl)
    Logger.debug("Shortened = " + shortened)

    promise.success(shortened)
    promise.future
  }


}
