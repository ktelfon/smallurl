package lv.home.smallurl.urlcompacter.controller

import lv.home.smallurl.WebIntegrationSpec
import lv.home.smallurl.urlcompacter.domain.SmallUrl
import lv.home.smallurl.urlcompacter.model.SmallUrlHolder
import lv.home.smallurl.urlcompacter.model.UrlStats
import lv.home.smallurl.urlcompacter.repository.SmallUrlRepo
import org.springframework.beans.factory.annotation.Autowired

import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class StatisticsControllerSpec extends WebIntegrationSpec {

    private ArrayList<SmallUrl> smallUrls = [
            SmallUrl.builder()
                    .original("1")
                    .compressed("a")
                    .count(1)
                    .build(),
            SmallUrl.builder()
                    .original("2")
                    .compressed("b")
                    .count(1)
                    .build(),
            SmallUrl.builder()
                    .original("3")
                    .compressed("c")
                    .count(1)
                    .build(),
            SmallUrl.builder()
                    .original("4")
                    .compressed("d")
                    .count(2)
                    .build(),
            SmallUrl.builder()
                    .original("5")
                    .compressed("e")
                    .count(3)
                    .build(),
            SmallUrl.builder()
                    .original("6")
                    .compressed("f")
                    .count(4)
                    .build()
    ]

    @Autowired
    SmallUrlRepo smallUrlRepo

    void setup() {
        smallUrlRepo.saveAll(smallUrls)
    }

    def "get stat by compressed url"() {

        given:
        def compressedUrl = smallUrls[0].compressed
        def request = HttpRequest.newBuilder(URI.create(baseUrl + "/stats/" + compressedUrl))
                .headers("Content-Type", APPLICATION_JSON_VALUE)
                .GET()
                .build()

        when:
        def resp = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())


        def response = resp.get()
        then:
        response.statusCode() == 200
        response.body() != null
        UrlStats result = objectMapper.readValue(response.body(), UrlStats)
        result.rank == 3
        result.count == smallUrls[0].count
        result.original == smallUrls[0].original
    }

    def "get all stats with page params page from middle"() {

        given:

        def page = 2
        def count = 2

        def request = HttpRequest.newBuilder(URI.create(baseUrl + "/stats?page=" + page + "&count=" + count))
                .headers("Content-Type", APPLICATION_JSON_VALUE)
                .GET()
                .build()

        when:
        def resp = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())


        def response = resp.get()
        then:
        response.statusCode() == 200
        response.body() != null
        List<UrlStats> result = objectMapper.readValue(response.body(), List<UrlStats>)
        result[0].rank == 3
        result[0].count == smallUrls[3].count
        result[0].original == smallUrls[3].original
    }

    def "get all stats with page params all saved data"() {

        given:

        def page = 1
        def count = 6

        def request = HttpRequest.newBuilder(URI.create(baseUrl + "/stats?page=" + page + "&count=" + count))
                .headers("Content-Type", APPLICATION_JSON_VALUE)
                .GET()
                .build()

        when:
        def resp = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())


        def response = resp.get()
        then:
        response.statusCode() == 200
        response.body() != null
        List<UrlStats> result = objectMapper.readValue(response.body(), List<UrlStats>)
        result[0].rank == 1
        result[0].count == smallUrls[5].count
        result[0].original == smallUrls[5].original
    }
}
