package lv.home.smallurl.urlcompacter.service;

import lombok.RequiredArgsConstructor;
import lv.home.smallurl.urlcompacter.domain.SmallUrl;
import lv.home.smallurl.urlcompacter.exception.NoSuchSmallUrlFoundException;
import lv.home.smallurl.urlcompacter.model.GeneratedUrl;
import lv.home.smallurl.urlcompacter.repository.SmallUrlRepo;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SmallUrlService {

    private final UrlCompacterService urlCompacterService;
    private final CounterService counterService;
    private final SmallUrlRepo smallUrlRepo;

    public GeneratedUrl generateUrl(String url) {
        String compressedIp = urlCompacterService.compressIp(url);
        smallUrlRepo.save(SmallUrl.builder()
                .count(1)
                .compressed(compressedIp)
                .original(url)
                .build());
        return new GeneratedUrl(compressedIp);
    }

    public String getOriginalUrl(String compressedUrl) throws NoSuchSmallUrlFoundException {
        SmallUrl smallUrl = smallUrlRepo.findByCompressed(compressedUrl)
                .orElseThrow(NoSuchSmallUrlFoundException::new);
        counterService.countLinkRequest(smallUrl);
        return smallUrl.getOriginal();
    }
}
