package lv.home.smallurl

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.net.http.HttpClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class WebIntegrationSpec extends Specification{

    def httpClient = HttpClient.newHttpClient()

    @Autowired
    ObjectMapper objectMapper

    @LocalServerPort
    String port

    String getBaseUrl(){
        'http://localhost:' + port
    }

    void await(double seconds, Closure<?> conditions) {
        new PollingConditions().within(seconds, conditions)
    }
}
