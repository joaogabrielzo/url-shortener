package com.zo.route

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.zo.service.ShortenerActor.{CreateShortUrl, RetrieveShortUrl}
import com.roundeights.hasher.Implicits._
import akka.pattern.ask
import com.zo.service.{ShortenerActor, UrlResponse, UrlResponseProtocol}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ShortenerRoute extends UrlResponseProtocol
                      with SprayJsonSupport {
    
    lazy val testing = "just for test purposes"
    
    implicit val system: ActorSystem = ActorSystem("shortener-route")
    implicit val timeout: Timeout = Timeout(3 seconds)
    
    val shortenerActor: ActorRef = system.actorOf(Props[ShortenerActor], "shortener-actor")
    
    val shortenerRoute: Route =
        (path("api" / "shortener") & post) {
            parameter("url", "short".?) { (url, short) =>
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    val shortKey = short.getOrElse(url.sha1.hex.slice(0, 8))
                    val shortUrlFuture = (shortenerActor ? CreateShortUrl(shortKey, url)).mapTo[UrlResponse]
                    
                    complete(shortUrlFuture)
                } else {
                    complete(HttpResponse(StatusCodes.BadRequest, entity = "Insert a valid URL."))
                }
            }
        } ~
        (path("goto" / Segment) & get) { hashKey =>
            val redirectFuture: Future[Option[String]] =
                (shortenerActor ? RetrieveShortUrl(hashKey)).mapTo[Option[String]]
            
            onComplete(redirectFuture) {
                case Success(request) =>
                    request match {
                        case Some(url) => redirect(url, StatusCodes.PermanentRedirect)
                        case None      => complete(StatusCodes.NotFound)
                    }
                case Failure(ex)      => complete(StatusCodes.BadRequest)
            }
            
        }
}
