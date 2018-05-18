package network.giantpay.api.graviex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.giantpay.dto.MarketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;

@Component
public class GraviexApi {

    private final static Logger logger = LoggerFactory.getLogger(GraviexApi.class);
    private ObjectMapper jsonMapper = new ObjectMapper();

    public MarketDto getMarketInfo() {
        MarketDto market = new MarketDto();
        try {
            JsonNode marketJson = jsonMapper.readTree(new URL("https://graviex.net/api/v2/tickers/gicbtc.json"));
            if (marketJson != null) {
                JsonNode tickerJson = marketJson.get("ticker");
                if (tickerJson != null) {
                    market.setBuy(BigDecimal.valueOf(tickerJson.get("buy").asDouble()));
                    market.setSell(BigDecimal.valueOf(tickerJson.get("sell").asDouble()));
                    market.setLow(BigDecimal.valueOf(tickerJson.get("low").asDouble()));
                    market.setHigh(BigDecimal.valueOf(tickerJson.get("high").asDouble()));
                    market.setLast(BigDecimal.valueOf(tickerJson.get("last").asDouble()));
                    market.setVolumeGic(BigDecimal.valueOf(tickerJson.get("vol").asDouble()));
                    market.setVolumeBtc(BigDecimal.valueOf(tickerJson.get("volbtc").asDouble()));
                    market.setChange(BigDecimal.valueOf(tickerJson.get("change").asDouble()));
                }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
        return market;
    }

}
