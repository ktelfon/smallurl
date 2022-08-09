package lv.home.smallurl.urlcompacter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.home.smallurl.urlcompacter.domain.SmallUrl;
import lv.home.smallurl.urlcompacter.exception.NoSuchSmallUrlFoundException;
import lv.home.smallurl.urlcompacter.model.UrlStats;
import lv.home.smallurl.urlcompacter.repository.CounterRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatsService {

    private final CounterRepo counterRepo;

    public Long getUrlRankByCounterId(long id) {
        log.info("Getting rank for counter id {} ", id);
        return counterRepo.getUrlRankById(id).orElseGet(() -> {
            log.info("No rank found for id {} ", id);
            return 0L;
        });
    }

    public UrlStats getStatsByCompressedUrl(String compressedUrl) throws NoSuchSmallUrlFoundException {
        log.info("Getting stats by compressed url {}", compressedUrl);
        return counterRepo.findBySmallUrl_Compressed(compressedUrl)
                .flatMap(counter -> Optional.of(UrlStats.builder()
                        .rank(getUrlRankByCounterId(counter.getId()))
                        .count(counter.getCount())
                        .link(counter.getSmallUrl().getCompressed())
                        .original(counter.getSmallUrl().getOriginal())
                        .build()))
                .orElseThrow(() -> {
                    log.info("Cannot get stats for {} ", compressedUrl);
                    return new NoSuchSmallUrlFoundException();
                });

    }
}
