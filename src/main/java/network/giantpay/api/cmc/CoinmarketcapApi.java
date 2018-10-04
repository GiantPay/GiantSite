package network.giantpay.api.cmc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.giantpay.dto.InfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class CoinmarketcapApi {

    private final static Logger logger = LoggerFactory.getLogger(CoinmarketcapApi.class);
    private final static ObjectMapper mapper = new ObjectMapper();

    public InfoDto getChanges() {
        InfoDto info = new InfoDto();
        try {
            JsonNode json = mapper.readTree(new URL("https://api.coinmarketcap.com/v2/ticker/3104/?convert=BTC"));
            if (json.has("data")) {
                JsonNode data = json.get("data");
                if (data.has("quotes")) {
                    JsonNode quotes = data.get("quotes");
                    if (quotes.has("USD")) {
                        JsonNode usd = quotes.get("USD");
                        info.setChangePrice24h(usd.get("percent_change_24h").decimalValue());
                    }
                }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
        return info;
    }

}
