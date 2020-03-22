package com.zo

import akka.actor.{Actor, ActorLogging}
import com.redis._
import com.zo.ShortenerActor._
import spray.json._


case class UrlResponse(oldUrl: String, shortUrl: String)

trait UrlResponseProtocol extends DefaultJsonProtocol {
    
    implicit val urlResponseFormat: RootJsonFormat[UrlResponse] = jsonFormat2(UrlResponse)
}

object ShortenerActor {
    case class CreateShortUrl(shortKey: String, urlValue: String)
    case class RetrieveShortUrl(shortKey: String)
}

class ShortenerActor extends Actor with ActorLogging {
    
    val redis = new RedisClient("localhost", 6379)
    
    override def receive: Receive = {
        case CreateShortUrl(shortKey, urlValue) =>
            log.info(s"Creating a short URL for $urlValue")
            redis.set(shortKey, urlValue)
            sender() ! UrlResponse(urlValue, "http://localhost:9999/short/" + shortKey)
        case RetrieveShortUrl(shortKey)         =>
            log.info("Redirecting...")
            val url: Option[String] = redis.get(shortKey)
            sender() ! url
    }
}
