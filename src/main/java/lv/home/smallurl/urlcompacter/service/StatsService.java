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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatsService {

    private final SmallUrlRepo smallUrlRepo;

    public UrlStats getStatsByCompressedUrl(String compressedUrl) throws NoSuchSmallUrlFoundException {
        log.info("Getting stats by compressed url {}", compressedUrl);

        // не могу предумать как сразу находить позицию
        // можно сделать sql с сортировкой и позицией в таблице
        // можно сделать ограниченый запрос что-бы не всё вытаскивать
        SmallUrl smallUrl = smallUrlRepo.findByCompressed(compressedUrl).orElseThrow(() -> {
            log.error("No compressed url found {}", compressedUrl);
            return new NoSuchSmallUrlFoundException();
        });
        List<SmallUrl> all = smallUrlRepo.findAll().stream()
                .sorted(Comparator.comparing(SmallUrl::getCount).reversed())
                .collect(Collectors.toList());
        int tmp = 0;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(smallUrl.getId())) {
                tmp = i;
            }
        }
        int rank = tmp;
        return UrlStats.builder()
                .rank(rank)
                .count(smallUrl.getCount())
                .link(smallUrl.getCompressed())
                .original(smallUrl.getOriginal())
                .build();
    }

    public List<UrlStats> getStatsByPage(int page, int count) {
        AtomicLong startingIndex = new AtomicLong((long) (page - 1) * count);
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
