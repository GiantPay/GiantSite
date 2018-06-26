package network.giantpay.api.cryptobridge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.giantpay.api.graviex.GraviexApi;
import network.giantpay.dto.MarketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;

@Component
public class CryptobridgeApi {

    private final static Logger logger = LoggerFactory.getLogger(GraviexApi.class);
    private ObjectMapper jsonMapper = new ObjectMapper();

    public MarketDto getMarketInfo() {
        MarketDto market = new MarketDto();
        try {
            JsonNode marketJson = jsonMapper.readTree(new URL("https://api.crypto-bridge.org/api/v1/ticker"));
            if (marketJson != null) {
                marketJson.forEach(ticker -> {
                    String id = ticker.get("id").asText();
                    if ("GIC_BTC".equals(id)) {
                        market.setBuy(BigDecimal.valueOf(ticker.get("bid").asDouble()));
                        market.setSell(BigDecimal.valueOf(ticker.get("ask").asDouble()));
                        market.setLast(BigDecimal.valueOf(ticker.get("last").asDouble()));
                        market.setVolumeBtc(BigDecimal.valueOf(ticker.get("volume").asDouble()));
                    }
                });
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
        return market;
    }
}
