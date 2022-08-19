package lv.home.smallurl.urlcompacter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.home.smallurl.urlcompacter.domain.SmallUrl;
import lv.home.smallurl.urlcompacter.exception.NoSuchSmallUrlFoundException;
import lv.home.smallurl.urlcompacter.model.UrlStats;
import lv.home.smallurl.urlcompacter.repository.SmallUrlRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatsService {

    private final SmallUrlRepo smallUrlRepo;

    public Long getUrlRankByCounterId(long id) {
        log.info("Getting rank for counter id {} ", id);
        return smallUrlRepo.getUrlRankById(id).orElseGet(() -> {
            log.info("No rank found for id {} ", id);
            return 0L;
        });
    }

    public UrlStats getStatsByCompressedUrl(String compressedUrl) throws NoSuchSmallUrlFoundException {
        log.info("Getting stats by compressed url {}", compressedUrl);
        return smallUrlRepo.findByCompressed(compressedUrl)
                .flatMap(smallUrl -> Optional.of(UrlStats.builder()
                        .rank(getUrlRankByCounterId(smallUrl.getId()))
                        .count(smallUrl.getCount())
                        .link(smallUrl.getCompressed())
                        .original(smallUrl.getOriginal())
                        .build()))
                .orElseThrow(() -> {
                    log.info("Cannot get stats for {} ", compressedUrl);
                    return new NoSuchSmallUrlFoundException();
                });
    }

    public List<UrlStats> getStatsByPage(int page, int count) {
        AtomicLong startingIndex = new AtomicLong((long) (page-1) * count);
        return smallUrlRepo
                .findAll(PageRequest.of((page - 1), count, Sort.by(Sort.Direction.DESC, "count")))
                .stream()
                .sorted(Comparator.comparing(SmallUrl::getCount).reversed())
                .map(smallUrl -> {
                    startingIndex.getAndIncrement();
                    return UrlStats.builder()
                            .link(smallUrl.getCompressed())
                            .original(smallUrl.getOriginal())
                            .rank(startingIndex.get())
                            .count(smallUrl.getCount())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
