package com.zo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.zo.ShortenerRoute.shortenerRoute

object ShortenerServer extends App {
    
    implicit val system: ActorSystem = ActorSystem("shortener-server")
    
    Http().bindAndHandle(shortenerRoute, "localhost", 9999)
}
