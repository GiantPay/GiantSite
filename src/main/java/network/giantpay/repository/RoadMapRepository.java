package network.giantpay.repository;

import network.giantpay.model.RoadMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadMapRepository extends JpaRepository<RoadMap, Long> {

    List<RoadMap> findAllByOrderByDateAsc();
}
