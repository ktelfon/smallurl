package lv.home.smallurl.urlcompacter.controller

import lv.home.smallurl.WebIntegrationSpec
import lv.home.smallurl.urlcompacter.model.SmallUrlHolder

import lv.home.smallurl.urlcompacter.repository.SmallUrlRepo
import org.springframework.beans.factory.annotation.Autowired

import java.net.http.HttpResponse


class SmallUrlControllerSpec extends WebIntegrationSpec {

    @Autowired
    SmallUrlRepo smallUrlRepo

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
            smallUrls[0].count == 1
        }
    }
}
