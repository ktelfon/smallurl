package lv.home.smallurl.urlcompacter.repository;

import lv.home.smallurl.urlcompacter.domain.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CounterRepo extends JpaRepository<Counter, Long> {

    Optional<Counter> findBySmallUrl_Compressed(String compressedUrl);

    @Query(value = "SELECT RowNr\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "         ROW_NUMBER() OVER (ORDER BY count) AS RowNr,\n" +
            "         id\n" +
            "    FROM counter\n" +
            ") sub\n" +
            "WHERE sub.id = :counterId\n", nativeQuery = true)
    Optional<Long> getUrlRankById(@Param("counterId") long counterId);
}
