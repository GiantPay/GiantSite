package network.giantpay.repository;

import network.giantpay.model.PresaleRequest;
import network.giantpay.model.PresaleRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PresaleRequestRepository extends JpaRepository<PresaleRequest, Long> {

    PresaleRequest findByGuid(String guid);

    @Query(value = "SELECT * FROM presale_requests WHERE status IN ('INIT','UNCONFIRMED')", nativeQuery = true)
    List<PresaleRequest> findUnconfirmed();
}
