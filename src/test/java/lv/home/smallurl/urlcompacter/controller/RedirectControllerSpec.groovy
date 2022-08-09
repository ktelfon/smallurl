package lv.home.smallurl.urlcompacter.controller

import lv.home.smallurl.WebIntegrationSpec
import lv.home.smallurl.urlcompacter.domain.Counter
import lv.home.smallurl.urlcompacter.domain.SmallUrl
import lv.home.smallurl.urlcompacter.repository.CounterRepo
import lv.home.smallurl.urlcompacter.repository.SmallUrlRepo
import org.springframework.beans.factory.annotation.Autowired

import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class RedirectControllerSpec extends WebIntegrationSpec {

    @Autowired
    SmallUrlRepo smallUrlRepo

    @Autowired
    CounterRepo counterRepo


    def "Redirects to a address from a saved url"() {

        def compressedUrl = '123'
        def link = "www.delfi.lv"
        given:
        def smallUrl = SmallUrl.builder()
                .compressed(compressedUrl)
                .original(link)
                .count(null)
                .build()
        smallUrl = smallUrlRepo.save(smallUrl)

        def count = counterRepo.save(Counter.builder()
                .count(1)
                .smallUrl(smallUrl)
                .build())

        counterRepo.save(count)

        when:

        def request = HttpRequest.newBuilder(URI.create(baseUrl + "/l/" + compressedUrl))
                .headers("Content-Type", APPLICATION_JSON_VALUE)
                .GET()
                .build()
        def resp = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())

        then:

        resp.get().statusCode() == 302
        resp.get().headers()['location'] == link

    }
}
