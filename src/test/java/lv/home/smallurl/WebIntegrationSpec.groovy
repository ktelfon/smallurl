package lv.home.smallurl

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.net.http.HttpClient
import java.net.http.HttpRequest

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

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

    public HttpRequest post(String path, String requestBody) {
        HttpRequest.newBuilder(URI.create(baseUrl + path))
                .headers("Content-Type", APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build()
    }

    public String jsonInString(Object anyObject) {
        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(anyObject)
    }
}
