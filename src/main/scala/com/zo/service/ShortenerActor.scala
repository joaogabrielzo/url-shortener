package com.zo.service

import akka.actor.{Actor, ActorLogging}
import com.redis._
import com.zo.service.ShortenerActor._
import com.zo._
import spray.json._


case class UrlResponse(oldUrl: String, shortUrl: String)

trait UrlResponseProtocol extends DefaultJsonProtocol {
    
    implicit val urlResponseFormat: RootJsonFormat[UrlResponse] = jsonFormat2(UrlResponse)
}

object ShortenerActor {
    case class CreateShortUrl(shortKey: String, urlValue: String)
    case class RetrieveShortUrl(shortKey: String)
}

class ShortenerActor extends Actor
                     with ActorLogging
                     with RedisConfig {
    
    val redis = new RedisClient(redisHost, redisPort)
    
    override def receive: Receive = {
        case CreateShortUrl(shortKey, urlValue) =>
            log.info(s"Creating a short URL for $urlValue")
            redis.set(shortKey, urlValue)
            sender() ! UrlResponse(urlValue, "http://localhost:9999/goto/" + shortKey)
        case RetrieveShortUrl(shortKey)         =>
            log.info("Redirecting...")
            val url: Option[String] = redis.get(shortKey)
            sender() ! url
    }
}
