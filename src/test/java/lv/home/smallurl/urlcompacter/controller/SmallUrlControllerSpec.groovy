package lv.home.smallurl.urlcompacter.controller

import lv.home.smallurl.WebIntegrationSpec
import lv.home.smallurl.urlcompacter.model.SmallUrlHolder
import lv.home.smallurl.urlcompacter.repository.CounterRepo
import lv.home.smallurl.urlcompacter.repository.SmallUrlRepo
import org.springframework.beans.factory.annotation.Autowired

import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class SmallUrlControllerSpec extends WebIntegrationSpec {

    @Autowired
    SmallUrlRepo smallUrlRepo

    @Autowired
    CounterRepo counterRepo

    def "On post request to '/generate' smallUrl is saved to DB"() {

        given:
        def link = "link"
        def linkReq = new SmallUrlHolder(link);
        def requestBody = jsonInString(linkReq)
        def request = post("/generate", requestBody)

        when:
        def resp = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())

        then:
        resp.get().statusCode() == 200
        resp.get().body() != null
        await(2) {
            def smallUrls = smallUrlRepo.findAll()
            smallUrls.size() == 1
            smallUrls[0].original == link

            def counters = counterRepo.findAll()
            counters.size() == 1
            counters[0].smallUrl.original == link
        }
    }

    private HttpRequest post(String path, String requestBody) {
        HttpRequest.newBuilder(URI.create(baseUrl + path))
                .headers("Content-Type", APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build()
    }

    private String jsonInString(Object anyObject) {
        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(anyObject)
    }
}
