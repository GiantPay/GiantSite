package network.giantpay.repository;

import network.giantpay.model.UtmSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UtmSourceRepository extends JpaRepository<UtmSource, Long> {

    @Query(nativeQuery = true, value = "SELECT us.url FROM utm_sources AS us WHERE us.source = ? OR us.source IS NULL ORDER BY us.source DESC NULLS LAST LIMIT 1")
    String findBySource(String source);
}
