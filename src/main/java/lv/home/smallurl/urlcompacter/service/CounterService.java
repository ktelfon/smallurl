package lv.home.smallurl.urlcompacter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.home.smallurl.urlcompacter.domain.Counter;
import lv.home.smallurl.urlcompacter.domain.SmallUrl;
import lv.home.smallurl.urlcompacter.repository.CounterRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class CounterService {

    private final CounterRepo counterRepo;

    @Async
    public Future<Counter> countLinkRequest(SmallUrl smallUrl) {
        return new AsyncResult<>(
                counterRepo.findBySmallUrl_Compressed(smallUrl.getCompressed())
                        .flatMap(counter -> {
                            counter.increment();
                            log.info("Updating {} counter ", smallUrl.getCompressed());
                            return Optional.of(counterRepo.save(counter));
                        })
                        .orElseGet(() -> {
                            log.info("Saving {} counter ", smallUrl.getCompressed());
                            return counterRepo.save(Counter.builder()
                                    .count(1)
                                    .smallUrl(smallUrl)
                                    .build());
                        }));
    }
}
