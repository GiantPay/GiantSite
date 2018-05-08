package network.giantpay.repository;

import network.giantpay.model.PresaleSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PresaleSettingRepository extends JpaRepository<PresaleSetting, Long> {

    @Query(value = "SELECT * FROM presale_settings LIMIT 1", nativeQuery = true)
    PresaleSetting findSetting();
}
