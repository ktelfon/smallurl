package lv.home.smallurl.urlcompacter.repository;

import lv.home.smallurl.urlcompacter.domain.SmallUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmallUrlRepo extends JpaRepository<SmallUrl, Long> {
    Optional<SmallUrl> findByCompressed(String compressedUrl);
}
