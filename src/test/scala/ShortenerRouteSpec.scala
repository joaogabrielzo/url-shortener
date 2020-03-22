import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.zo.service.UrlResponseProtocol
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ShortenerRouteSpec extends AnyWordSpec
                         with Matchers
                         with ScalatestRouteTest
                         with UrlResponseProtocol {
    
}
