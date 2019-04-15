package network.giantpay.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import network.giantpay.api.WalletException;
import network.giantpay.api.cmc.CoinmarketcapApi;
import network.giantpay.api.cryptobridge.CryptobridgeApi;
import network.giantpay.api.giant.GiantExplorer;
import network.giantpay.api.giant.GiantWallet;
import network.giantpay.api.graviex.GraviexApi;
import network.giantpay.api.trello.TrelloApi;
import network.giantpay.dto.*;
import network.giantpay.dto.trello.Board;
import network.giantpay.utils.GiantUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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

    private AtomicLong height = new AtomicLong(0);
    private AtomicReference<BigDecimal> reward = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> networkHashrate = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> networkDifficulty = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> coinSupply = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicLong masternodes = new AtomicLong(0);
    private AtomicReference<RoiDto> masternodeRoi = new AtomicReference<>(RoiDto.EMPTY);
    private AtomicReference<BigDecimal> masternodeRoiDays = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<List<MasternodeDto>> masternodesList = new AtomicReference<>(ImmutableList.of());
    private Map<String, CoinInfoDto> coinInfos = Maps.newConcurrentMap();
    private AtomicReference<BigDecimal> gicBtc = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> gicUsd = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> btcUsd = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> btcVolume = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> masternodeDaily = new AtomicReference<>(BigDecimal.ONE);
    private AtomicReference<BigDecimal> masternodeMonthly = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> masternodeAnnual = new AtomicReference<>(BigDecimal.ZERO);
    private Map<String, MarketDto> markets = Maps.newConcurrentMap();
    private AtomicReference<Board> trelloBoard = new AtomicReference<>();
    private AtomicReference<BigDecimal> changePrice24h = new AtomicReference<>(BigDecimal.ZERO);
    private AtomicReference<BigDecimal> changeVolume24h = new AtomicReference<>(BigDecimal.ZERO);

    @Autowired
    private GiantWallet giantWallet;
    @Autowired
    private GiantExplorer giantExplorer;
    @Autowired
    private GraviexApi graviexApi;
    @Autowired
    private CryptobridgeApi cryptobridgeApi;
    @Autowired
    private TrelloApi trelloApi;
    @Autowired
    private CoinmarketcapApi coinmarketcapApi;

    @PostConstruct
    public void initialize() {
        coinInfos.put("BTC", new CoinInfoDto("Bitcoin", "BTC", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        coinInfos.put("ETH", new CoinInfoDto("Ethereum", "ETH", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        coinInfos.put("XRP", new CoinInfoDto("Ripple", "XRP", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        coinInfos.put("LTC", new CoinInfoDto("Litecoin", "LTC", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        coinInfos.put("DASH", new CoinInfoDto("Dash", "DASH", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        coinInfos.put("BCH", new CoinInfoDto("Bitcoin Cash", "BCH", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

        gicBtc.set(BigDecimal.valueOf(0.0006));
        btcUsd.set(BigDecimal.valueOf(9000D));
    }

    public InfoDto getInfo() {
        final CoinInfoDto btc = getCoinInfo("BTC");

        final InfoDto info = new InfoDto();
        info.setRate(gicBtc.get());
        info.setChangePrice24h(changePrice24h.get());
        info.setVolume(btcVolume.get());
        info.setUsdVolume(GiantUtils.getUsdVolume(btc,info.getVolume()));
        info.setChangeVolume24h(changeVolume24h.get());
        info.setHeight(height.get());
        info.setReward(reward.get());
        info.setNetworkHashrate(networkHashrate.get());
        info.setNetworkDifficulty(networkDifficulty.get());
        info.setCoinSupply(coinSupply.get());
        info.setMasternodes(masternodes.get());
        info.setMasternodeRoi(masternodeRoi.get().getPerYear());
        info.setMasternodeRoiDays(masternodeRoiDays.get());

        return info;
    }

  //  @Scheduled(initialDelay = 10000, fixedRate = 3600000)
    private void updateChanges() {
        InfoDto changes = coinmarketcapApi.getChanges();
        changePrice24h.set(changes.getChangePrice24h());
    }

 //   @Scheduled(initialDelay = 10000, fixedRate = 60000)
    private void updateRates() {
        try {
            logger.info("MonitoringService :: updateRates started");

            // TODO calculate aggregate price from one or more exchanges API
            BigDecimal gicRate = BigDecimal.ZERO;
            BigDecimal gicVolume = BigDecimal.ZERO;

            MarketDto graviexMarket = graviexApi.getMarketInfo();
            if (graviexMarket != null && graviexMarket.getLast() != null) {
                gicRate = gicRate.add(graviexMarket.getLast().multiply(graviexMarket.getVolumeBtc()));
                gicVolume = gicVolume.add(graviexMarket.getVolumeBtc());

                markets.put("graviex", graviexMarket);

                logger.info("MonitoringService :: updateRates graviex {}", graviexMarket);
            }

            MarketDto cryptobridgeMarket = cryptobridgeApi.getMarketInfo();
            if (cryptobridgeMarket != null && cryptobridgeMarket.getLast() != null) {
                gicRate = gicRate.add(cryptobridgeMarket.getLast().multiply(cryptobridgeMarket.getVolumeBtc()));
                gicVolume = gicVolume.add(cryptobridgeMarket.getVolumeBtc());

                markets.put("cryptobridge", cryptobridgeMarket);

                logger.info("MonitoringService :: updateRates cryptobridge {}", cryptobridgeMarket);
            }

            gicBtc.set(gicRate.divide(gicVolume, 8, RoundingMode.HALF_UP));
            gicUsd.set(gicBtc.get().multiply(btcUsd.get()));
            btcVolume.set(gicVolume);

            logger.info("MonitoringService :: updateRates gic/btc= {}, gic/usd= {}", gicBtc.get(), gicUsd.get());
            logger.info("MonitoringService :: updateRates finished");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

 //   @Scheduled(initialDelay = 10000, fixedRate = 60000)
    private void updateNetworkInfo() {
        try {
            logger.info("MonitoringService :: updateNetworkInfo started");

            OutsetInfoDto info = giantWallet.gettxoutsetinfo();

            height.set(getHeight(info));
            reward.set(getBlockReward(height.get()));
            networkHashrate.set(getNetworkHashrate());
            networkDifficulty.set(getNetworkDifficulty());
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

  //  @Scheduled(initialDelay = 10000, fixedRate = 3600000)
    private void updateTrello() {
        trelloBoard.set(trelloApi.getBoard());
    }

    private BigDecimal getNetworkDifficulty() throws WalletException {
        BigDecimal networkDifficulty = giantExplorer.getdifficulty();
        return networkDifficulty != null && networkDifficulty.doubleValue() > 0 ? networkDifficulty : giantWallet.getdifficulty();
    }

    private BigDecimal getNetworkHashrate() throws WalletException {
        BigDecimal networkHashrate = giantExplorer.getnetworkhashps();
        return networkHashrate != null && networkHashrate.longValue() > 0 ? networkHashrate : giantWallet.getnetworkhashps(height.get());
    }

    private long getHeight(OutsetInfoDto info) {
        BigDecimal height = giantExplorer.getblockcount();
        return height != null && height.longValue() > 0 ? height.longValue() : info.getHeight();
    }

   // @Scheduled(initialDelay = 10000, fixedRate = 60000)
    private void updateCoinInfos() {
        try {
            logger.info("MonitoringService :: updateCoinInfos started");

            JsonNode data = jsonMapper.readTree(new URL("https://api.coinmarketcap.com/v2/ticker/?convert=BTC&limit=30"));
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

                    btcUsd.set(btcInfo.getPrice());
                    gicUsd.set(gicBtc.get().multiply(btcInfo.getPrice()));

                    logger.info("MonitoringService :: updateCoinInfos {}", btcInfo);
                    logger.info("MonitoringService :: btcUsd= {}", btcUsd.get());
                    logger.info("MonitoringService :: gicUsd= {}", gicUsd.get());
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

    //@Scheduled(initialDelay = 10000, fixedRate = 60000)
    private void updateMasternodes() {
        try {
            logger.info("MonitoringService :: updateMaster nodes started");

            List<MasternodeDto> masternodes = giantWallet.masternodelist();
            this.masternodesList.set(masternodes);
            this.masternodes.set(masternodes.stream()
                    .filter(masternode -> "ENABLED".equals(masternode.getStatus()))
                    .count());

            // annual ROI
            BigDecimal masternodeReward = GiantUtils.getMasternodeReward(height.get());

            if (this.masternodes.get() > 0) {
                masternodeRoi.set(RoiDto.fromMasterNodeReward(masternodeReward, this.masternodes.get()));

                // ROI days
                masternodeRoiDays.set(BigDecimal.valueOf(1000L) // 1000 colateral price
                        .divide(BigDecimal.valueOf(720).multiply(masternodeReward) // 720 blocks per day
                                .divide(BigDecimal.valueOf(this.masternodes.get()), 2, BigDecimal.ROUND_HALF_UP), 0, BigDecimal.ROUND_CEILING));

                masternodeDaily.set(BigDecimal.valueOf(720).multiply(masternodeReward).divide(BigDecimal.valueOf(this.masternodes.get()), 2, BigDecimal.ROUND_HALF_UP));
                masternodeMonthly.set(masternodeDaily.get().multiply(BigDecimal.valueOf(30)));
                masternodeAnnual.set(masternodeDaily.get().multiply(BigDecimal.valueOf(365)));
            }

            logger.info("MonitoringService :: updateMasternodes masternodes= {}, roi= {}, days= {}, daily= {}, monthly= {}, annual= {}",
                    this.masternodes.get(),
                    masternodeRoi.get(),
                    masternodeRoiDays.get(),
                    masternodeDaily.get(),
                    masternodeMonthly.get(),
                    masternodeAnnual.get());
            logger.info("MonitoringService :: update Masternodes finished");
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

    public RateDto getRates() {
        RateDto rateDto = new RateDto();
        rateDto.setBtc(gicBtc.get());
        rateDto.setUsd(gicUsd.get());
        return rateDto;
    }

    public MasternodeInfoDto getMasternodeInfo() {
        MasternodeInfoDto masternodeInfoDto = new MasternodeInfoDto();
        masternodeInfoDto.setDaily(masternodeDaily.get());
        masternodeInfoDto.setMonthly(masternodeMonthly.get());
        masternodeInfoDto.setAnnual(masternodeAnnual.get());
        masternodeInfoDto.setRoi(masternodeRoi.get());
        masternodeInfoDto.setDays(masternodeRoiDays.get());
        masternodeInfoDto.setMasternodeCount(masternodes.get());
        return masternodeInfoDto;
    }

    public BigDecimal getDailyIncome() {
        return masternodeDaily.get();
    }

    public long getEnabledMasternodes() {
        return masternodesList.get()
                .stream()
                .filter(m -> "ENABLED".equals(m.getStatus()))
                .count();
    }

    public Map<String, MarketDto> getMarkets() {
        return markets.isEmpty() ? onEmptyExchange() : markets;
    }

    public Board getTrelloBoard() {
        return trelloBoard.get();
    }


    private static Map<String, MarketDto> onEmptyExchange() {
        return ImmutableMap.of(
                "cryptobridge", MarketDto.builder().last(new BigDecimal(BigInteger.ONE)).volumeBtc(new BigDecimal(BigInteger.ONE)).build(),
                "graviex", MarketDto.builder().last(new BigDecimal(BigInteger.ONE)).volumeBtc(new BigDecimal(BigInteger.ONE)).build()
        );
    }
}
