package lv.home.smallurl.urlcompacter.controller;

import lombok.RequiredArgsConstructor;
import lv.home.smallurl.urlcompacter.exception.NoSuchSmallUrlFoundException;
import lv.home.smallurl.urlcompacter.service.SmallUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/l/")
public class RedirectController {

    private final SmallUrlService smallUrlService;

    @GetMapping("/{compressedUrl}")
    ResponseEntity<Void> redirect(String compressedUrl) throws NoSuchSmallUrlFoundException {
        String originalUrl = smallUrlService.getOriginalUrl(compressedUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
