package network.giantpay.service;

import lombok.AllArgsConstructor;
import network.giantpay.dto.RateDto;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class set Rates of Usd and Btc to given {@link Map}
 */
@Service
@AllArgsConstructor
public final class RateService implements Consumer<Map<String, Object>> {

    private final MonitoringService monitoringService;

    @Override
    public void accept(final Map<String, Object> model) {
        final RateDto rates = this.monitoringService.getRates();

        model.put("btcRate", rates.getBtc().setScale(8, RoundingMode.HALF_DOWN));
        model.put("usdRate", rates.getUsd().setScale(2, RoundingMode.HALF_DOWN));
    }
}
