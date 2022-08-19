package lv.home.smallurl.urlcompacter.controller

import lv.home.smallurl.WebIntegrationSpec

import lv.home.smallurl.urlcompacter.domain.SmallUrl

import lv.home.smallurl.urlcompacter.repository.SmallUrlRepo
import org.springframework.beans.factory.annotation.Autowired

import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class RedirectControllerSpec extends WebIntegrationSpec {

    @Autowired
    SmallUrlRepo smallUrlRepo



    def "Redirects to a address from a saved url"() {

        def compressedUrl = '123'
        def link = "www.delfi.lv"
        given:
        def smallUrl = SmallUrl.builder()
                .compressed(compressedUrl)
                .original(link)
                .count(1)
                .build()
        smallUrlRepo.save(smallUrl)

        when:

        def request = HttpRequest.newBuilder(URI.create(baseUrl + "/l/" + compressedUrl))
                .headers("Content-Type", APPLICATION_JSON_VALUE)
                .GET()
                .build()
        def resp = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())

        then:

        resp.get().statusCode() == 302
        resp.get().headers().allValues('location')[0] != null

    }
}
