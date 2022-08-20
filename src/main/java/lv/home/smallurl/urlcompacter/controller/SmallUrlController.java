package lv.home.smallurl.urlcompacter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.home.smallurl.urlcompacter.model.GeneratedUrl;
import lv.home.smallurl.urlcompacter.model.SmallUrlHolder;
import lv.home.smallurl.urlcompacter.service.SmallUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/generate")
public class SmallUrlController {

    private final SmallUrlService smallUrlService;

    @PostMapping
    public ResponseEntity<GeneratedUrl> generateSmallUrl(@Valid @RequestBody SmallUrlHolder smallUrlHolder) {
        log.info("Received " + smallUrlHolder.getOriginal());
        GeneratedUrl body = smallUrlService.generateUrl(smallUrlHolder.getOriginal());
        return ResponseEntity.ok(body);
    }

}
