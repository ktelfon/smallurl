package lv.home.smallurl.urlcompacter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.home.smallurl.urlcompacter.exception.NoSuchSmallUrlFoundException;
import lv.home.smallurl.urlcompacter.model.UrlStats;
import lv.home.smallurl.urlcompacter.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/stats")
public class StatisticsController {

    private final StatsService statsService;

    @GetMapping("/{compressedUrl}")
    public ResponseEntity<UrlStats> getUrlStats(@PathVariable(name = "compressedUrl") String compressedUrl) throws NoSuchSmallUrlFoundException {
        return ResponseEntity.ok(statsService.getStatsByCompressedUrl(compressedUrl));
    }

    @GetMapping
    public ResponseEntity<List<UrlStats>> getAllOfStats(@RequestParam int page, @RequestParam int count) {
        return ResponseEntity.ok(statsService.getStatsByPage(page, count));
    }
}
