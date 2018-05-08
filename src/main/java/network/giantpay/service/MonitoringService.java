package network.giantpay.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import network.giantpay.dto.CoinInfoDto;
import network.giantpay.dto.InfoDto;
import network.giantpay.dto.MasternodeDto;
import network.giantpay.dto.OutsetInfoDto;
import network.giantpay.rpc.giant.GiantWallet;
import network.giantpay.utils.GiantUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static network.giantpay.utils.GiantUtils.getBlockReward;

@Service
public class MonitoringService {

    private final static Logger logger = LoggerFactory.getLogger(MonitoringService.class);
    private final static ObjectMapper jsonMapper = new ObjectMapper();

    private AtomicReference<BigDecimal> rate = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicLong height = new AtomicLong(0);
    private AtomicReference<BigDecimal> reward = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> networkHashrate = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> networkDifficulty = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> coinSupply = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicLong masternodes = new AtomicLong(0);
    private AtomicReference<BigDecimal> masternodeRoi = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> masternodeRoiDays = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<List<MasternodeDto>> masternodesList = new AtomicReference<>(ImmutableList.of());
    private Map<String, CoinInfoDto> coinInfos = Maps.newConcurrentMap();

    @Autowired
    private GiantWallet giantWallet;

    @PostConstruct
    public void initialize() {
        coinInfos.put("BTC", new CoinInfoDto("Bitcoin", "BTC", BigDecimal.valueOf(7290), BigDecimal.valueOf(15.55), BigDecimal.valueOf(258087), BigDecimal.valueOf(923629)));
        coinInfos.put("ETH", new CoinInfoDto("Ethereum", "ETH", BigDecimal.valueOf(7290), BigDecimal.valueOf(15.55), BigDecimal.valueOf(258087), BigDecimal.valueOf(923629)));
        coinInfos.put("XRP", new CoinInfoDto("Ripple", "XRP", BigDecimal.valueOf(7290), BigDecimal.valueOf(15.55), BigDecimal.valueOf(258087), BigDecimal.valueOf(923629)));
        coinInfos.put("LTC", new CoinInfoDto("Litecoin", "LTC", BigDecimal.valueOf(7290), BigDecimal.valueOf(15.55), BigDecimal.valueOf(258087), BigDecimal.valueOf(923629)));
        coinInfos.put("DASH", new CoinInfoDto("Dash", "DASH", BigDecimal.valueOf(7290), BigDecimal.valueOf(15.55), BigDecimal.valueOf(258087), BigDecimal.valueOf(923629)));
        coinInfos.put("BCH", new CoinInfoDto("Bitcoin Cash", "BCH", BigDecimal.valueOf(7290), BigDecimal.valueOf(15.55), BigDecimal.valueOf(258087), BigDecimal.valueOf(923629)));
    }

    public InfoDto getInfo() {
        InfoDto info = new InfoDto();
        info.setRate(rate.get());
        info.setHeight(height.get());
        info.setReward(reward.get());
        info.setNetworkHashrate(networkHashrate.get());
        info.setNetworkDifficulty(networkDifficulty.get());
        info.setCoinSupply(coinSupply.get());
        info.setMasternodes(masternodes.get());
        info.setMasternodeRoi(masternodeRoi.get());
        info.setMasternodeRoiDays(masternodeRoiDays.get());
        return info;
    }

    @Scheduled(initialDelay = 10000, fixedRate = 60000)
    public void updateRates() {
        try {
            logger.info("MonitoringService :: updateRates started");

            // TODO currently presale price
            rate.set(BigDecimal.valueOf(0.0006));

            logger.info("MonitoringService :: updateRates rate = {}", rate.get());
            logger.info("MonitoringService :: updateRates finished");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Scheduled(initialDelay = 10000, fixedRate = 60000)
    public void updateNetworkInfo() {
        try {
            logger.info("MonitoringService :: updateNetworkInfo started");

            OutsetInfoDto info = giantWallet.gettxoutsetinfo();
            height.set(info.getHeight());
            reward.set(getBlockReward(info.getHeight()));
            networkHashrate.set(giantWallet.getnetworkhashps(info.getHeight()));
            networkDifficulty.set(giantWallet.getdifficulty());
            coinSupply.set(info.getCoinSupply());

            logger.info("MonitoringService :: updateNetworkInfo height= {}, reward= {}, hashrate= {}, difficulty= {}, coinSupply= {}",
                    height.get(),
                    reward.get(),
                    networkHashrate.get(),
                    networkDifficulty.get(),
                    coinSupply.get());
            logger.info("MonitoringService :: updateNetworkInfo finished");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Scheduled(initialDelay = 10000, fixedRate = 60000)
    public void updateCoinInfos() {
        try {
            logger.info("MonitoringService :: updateCoinInfos started");

            JsonNode data = jsonMapper.readTree(new URL("https://api.coinmarketcap.com/v2/ticker/?convert=BTC&limit=10"));
            // BTC
            if (data.has("data") && data.get("data").has("1")) {
                JsonNode btc = data.get("data").get("1");
                if (btc.has("quotes") && btc.get("quotes").has("USD")) {
                    JsonNode usdBtc = btc.get("quotes").get("USD");

                    CoinInfoDto btcInfo = new CoinInfoDto();
                    btcInfo.setName("Bitcoin");
                    btcInfo.setTicker("BTC");
                    btcInfo.setPrice(BigDecimal.valueOf(usdBtc.get("price").asDouble()));
                    btcInfo.setChange(BigDecimal.valueOf(usdBtc.get("percent_change_24h").asDouble()));
                    btcInfo.setVolume(BigDecimal.valueOf(usdBtc.get("volume_24h").asDouble()));
                    btcInfo.setSupply(BigDecimal.valueOf(btc.get("circulating_supply").asDouble()));
                    coinInfos.put("BTC", btcInfo);

                    logger.info("MonitoringService :: updateCoinInfos {}", btcInfo);
                }
            }
            // ETH
            if (data.has("data") && data.get("data").has("1027")) {
                JsonNode eth = data.get("data").get("1027");
                if (eth.has("quotes") && eth.get("quotes").has("USD")) {
                    JsonNode usdEth = eth.get("quotes").get("USD");

                    CoinInfoDto ethInfo = new CoinInfoDto();
                    ethInfo.setName("Ethereum");
                    ethInfo.setTicker("ETH");
                    ethInfo.setPrice(BigDecimal.valueOf(usdEth.get("price").asDouble()));
                    ethInfo.setChange(BigDecimal.valueOf(usdEth.get("percent_change_24h").asDouble()));
                    ethInfo.setVolume(BigDecimal.valueOf(usdEth.get("volume_24h").asDouble()));
                    ethInfo.setSupply(BigDecimal.valueOf(eth.get("circulating_supply").asDouble()));
                    coinInfos.put("ETH", ethInfo);

                    logger.info("MonitoringService :: updateCoinInfos {}", ethInfo);
                }
            }
            // XRP
            if (data.has("data") && data.get("data").has("52")) {
                JsonNode xrp = data.get("data").get("52");
                if (xrp.has("quotes") && xrp.get("quotes").has("USD")) {
                    JsonNode usdXrp = xrp.get("quotes").get("USD");

                    CoinInfoDto xrpInfo = new CoinInfoDto();
                    xrpInfo.setName("Ripple");
                    xrpInfo.setTicker("XRP");
                    xrpInfo.setPrice(BigDecimal.valueOf(usdXrp.get("price").asDouble()));
                    xrpInfo.setChange(BigDecimal.valueOf(usdXrp.get("percent_change_24h").asDouble()));
                    xrpInfo.setVolume(BigDecimal.valueOf(usdXrp.get("volume_24h").asDouble()));
                    xrpInfo.setSupply(BigDecimal.valueOf(xrp.get("circulating_supply").asDouble()));
                    coinInfos.put("XRP", xrpInfo);

                    logger.info("MonitoringService :: updateCoinInfos {}", xrpInfo);
                }
            }
            // LTC
            if (data.has("data") && data.get("data").has("2")) {
                JsonNode ltc = data.get("data").get("2");
                if (ltc.has("quotes") && ltc.get("quotes").has("USD")) {
                    JsonNode usdLtc = ltc.get("quotes").get("USD");

                    CoinInfoDto ltcInfo = new CoinInfoDto();
                    ltcInfo.setName("Litecoin");
                    ltcInfo.setTicker("LTC");
                    ltcInfo.setPrice(BigDecimal.valueOf(usdLtc.get("price").asDouble()));
                    ltcInfo.setChange(BigDecimal.valueOf(usdLtc.get("percent_change_24h").asDouble()));
                    ltcInfo.setVolume(BigDecimal.valueOf(usdLtc.get("volume_24h").asDouble()));
                    ltcInfo.setSupply(BigDecimal.valueOf(ltc.get("circulating_supply").asDouble()));
                    coinInfos.put("LTC", ltcInfo);

                    logger.info("MonitoringService :: updateCoinInfos {}", ltcInfo);
                }
            }
            // DASH
            if (data.has("data") && data.get("data").has("131")) {
                JsonNode dash = data.get("data").get("131");
                if (dash.has("quotes") && dash.get("quotes").has("USD")) {
                    JsonNode usdDash = dash.get("quotes").get("USD");

                    CoinInfoDto dashInfo = new CoinInfoDto();
                    dashInfo.setName("Dash");
                    dashInfo.setTicker("DASH");
                    dashInfo.setPrice(BigDecimal.valueOf(usdDash.get("price").asDouble()));
                    dashInfo.setChange(BigDecimal.valueOf(usdDash.get("percent_change_24h").asDouble()));
                    dashInfo.setVolume(BigDecimal.valueOf(usdDash.get("volume_24h").asDouble()));
                    dashInfo.setSupply(BigDecimal.valueOf(dash.get("circulating_supply").asDouble()));
                    coinInfos.put("DASH", dashInfo);

                    logger.info("MonitoringService :: updateCoinInfos {}", dashInfo);
                }
            }
            // BCH
            if (data.has("data") && data.get("data").has("1831")) {
                JsonNode bch = data.get("data").get("1831");
                if (bch.has("quotes") && bch.get("quotes").has("USD")) {
                    JsonNode usdBch = bch.get("quotes").get("USD");

                    CoinInfoDto bchInfo = new CoinInfoDto();
                    bchInfo.setName("Bitcoin Cash");
                    bchInfo.setTicker("BCH");
                    bchInfo.setPrice(BigDecimal.valueOf(usdBch.get("price").asDouble()));
                    bchInfo.setChange(BigDecimal.valueOf(usdBch.get("percent_change_24h").asDouble()));
                    bchInfo.setVolume(BigDecimal.valueOf(usdBch.get("volume_24h").asDouble()));
                    bchInfo.setSupply(BigDecimal.valueOf(bch.get("circulating_supply").asDouble()));
                    coinInfos.put("BCH", bchInfo);

                    logger.info("MonitoringService :: updateCoinInfos {}", bchInfo);
                }
            }
            logger.info("MonitoringService :: updateCoinInfos finished");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Scheduled(initialDelay = 10000, fixedRate = 60000)
    public void updateMasternodes() {
        try {
            logger.info("MonitoringService :: updateMasternodes started");

            List<MasternodeDto> masternodes = giantWallet.masternodelist();
            this.masternodesList.set(masternodes);
            this.masternodes.set(masternodes.stream()
                    .filter(masternode -> "ENABLED".equals(masternode.getStatus()))
                    .count());

            // annual ROI
            BigDecimal masternodeReward = GiantUtils.getMasternodeReward(height.get());
            // 720 blocks per day, 365 days annualy
            masternodeRoi.set(BigDecimal.valueOf(100 * 720 * 365)
                    .multiply(masternodeReward)
                    // 1000 colateral price
                    .divide(BigDecimal.valueOf(1000 * this.masternodes.get()), 2, BigDecimal.ROUND_HALF_UP));

            // ROI days
            masternodeRoiDays.set(BigDecimal.valueOf(1000L) // 1000 colateral price
                    .divide(BigDecimal.valueOf(720).multiply(masternodeReward) // 720 blocks per day
                                    .divide(BigDecimal.valueOf(this.masternodes.get()), 2, BigDecimal.ROUND_HALF_UP)
                            , 0, BigDecimal.ROUND_CEILING));

            logger.info("MonitoringService :: updateMasternodes masternodes= {}, roi= {}, days= {}",
                    this.masternodes.get(),
                    masternodeRoi.get(),
                    masternodeRoiDays.get());
            logger.info("MonitoringService :: updateMasternodes finished");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public List<MasternodeDto> getMasternodes() {
        return masternodesList.get();
    }

    public CoinInfoDto getCoinInfo(String ticker) {
        return coinInfos.get(ticker);
    }
}
