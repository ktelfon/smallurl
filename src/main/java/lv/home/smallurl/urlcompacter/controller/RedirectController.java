package lv.home.smallurl.urlcompacter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.home.smallurl.urlcompacter.exception.NoSuchSmallUrlFoundException;
import lv.home.smallurl.urlcompacter.service.SmallUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/l/")
public class RedirectController {

    private final SmallUrlService smallUrlService;

    @GetMapping("/{compressedUrl}")
    ResponseEntity<Void> redirect(@PathVariable(name = "compressedUrl") String compressedUrl) throws NoSuchSmallUrlFoundException {
        String originalUrl = smallUrlService.getOriginalUrl(compressedUrl);
        log.info("Redirecting to :" + originalUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
