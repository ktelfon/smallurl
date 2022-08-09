package lv.home.smallurl.urlcompacter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.home.smallurl.urlcompacter.exception.NoSuchSmallUrlFoundException;
import lv.home.smallurl.urlcompacter.model.UrlStats;
import lv.home.smallurl.urlcompacter.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/stats/")
public class StatisticsController {

    private final StatsService statsService;

    @GetMapping("{compressedUrl}")
    public ResponseEntity<UrlStats> getUrlStats(@PathVariable(name = "compressedUrl") String compressedUrl) throws NoSuchSmallUrlFoundException {
        return ResponseEntity.ok(statsService.getStatsByCompressedUrl(compressedUrl));
    }
}
