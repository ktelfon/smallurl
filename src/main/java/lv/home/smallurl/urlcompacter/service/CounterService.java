package lv.home.smallurl.urlcompacter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.home.smallurl.urlcompacter.domain.SmallUrl;
import lv.home.smallurl.urlcompacter.repository.SmallUrlRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class CounterService {

    private final SmallUrlRepo smallUrlRepo;

    @Async
    public Future<SmallUrl> countLinkRequest(SmallUrl smallUrl) {
        smallUrl.setCount(smallUrl.getCount() + 1);
        return new AsyncResult<>(
                smallUrlRepo.save(smallUrl)
        );
    }
}
