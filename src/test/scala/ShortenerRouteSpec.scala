import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.zo.route.ShortenerRoute._
import com.zo.service.{UrlResponse, UrlResponseProtocol}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration._

class ShortenerRouteSpec extends AnyWordSpec
                         with Matchers
                         with ScalatestRouteTest
                         with UrlResponseProtocol {
    
    "A url shortener" should {
        "return a JSON with the old and new shortened URL" in {
            Post("/api/shortener?url=http://github.com/joaogabrielzo&short=my-github") ~> shortenerRoute ~> check {
                status shouldBe StatusCodes.OK
                
                responseAs[UrlResponse] shouldBe UrlResponse("http://github.com/joaogabrielzo",
                    "http://localhost:9999/goto/my-github")
            }
        }
        "return a hash code when parameter `short` is not given" in {
            Post("/api/shortener?url=http://github.com/joaogabrielzo") ~> shortenerRoute ~> check {
                status shouldBe StatusCodes.OK
                
                responseAs[UrlResponse] shouldBe UrlResponse("http://github.com/joaogabrielzo",
                    "http://localhost:9999/goto/f21d9820")
            }
        }
        "return a Bad Request if the URL do not starts with http:// or https://" in {
            Post("/api/shortener?url=github.com/joaogabrielzo") ~> shortenerRoute ~> check {
                status shouldBe StatusCodes.BadRequest
                
                responseEntity.contentType shouldBe ContentTypes.`text/plain(UTF-8)`
            }
        }
        "redirect to a website when a GET request is made on an existing url alias" in {
            Get("/goto/my-github") ~> shortenerRoute ~> check {
                status shouldBe StatusCodes.PermanentRedirect
                
                response.headers shouldBe List(Location("http://github.com/joaogabrielzo"))
            }
        }
        "redirect to a website when a GET request is made on an existing url with hash code" in {
            Get("/goto/f21d9820") ~> shortenerRoute ~> check {
                status shouldBe StatusCodes.PermanentRedirect
                
                response.headers shouldBe List(Location("http://github.com/joaogabrielzo"))
            }
        }
        
    }
    
}
