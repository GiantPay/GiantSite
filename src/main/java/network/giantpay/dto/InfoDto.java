package network.giantpay.dto;

import java.math.BigDecimal;

public class InfoDto extends AbstractDto {

    private BigDecimal rate;
    private BigDecimal changePrice24h;
    private BigDecimal volume;
    private BigDecimal changeVolume24h;
    private long height;
    private BigDecimal reward;
    private BigDecimal networkHashrate;
    private BigDecimal networkDifficulty;
    private BigDecimal coinSupply;
    private long masternodes;
    private BigDecimal masternodeRoi;
    private BigDecimal masternodeRoiDays;

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getChangePrice24h() {
        return changePrice24h;
    }

    public void setChangePrice24h(BigDecimal changePrice24h) {
        this.changePrice24h = changePrice24h;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getChangeVolume24h() {
        return changeVolume24h;
    }

    public void setChangeVolume24h(BigDecimal changeVolume24h) {
        this.changeVolume24h = changeVolume24h;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public BigDecimal getNetworkHashrate() {
        return networkHashrate;
    }

    public void setNetworkHashrate(BigDecimal networkHashrate) {
        this.networkHashrate = networkHashrate;
    }

    public BigDecimal getNetworkDifficulty() {
        return networkDifficulty;
    }

    public void setNetworkDifficulty(BigDecimal networkDifficulty) {
        this.networkDifficulty = networkDifficulty;
    }

    public BigDecimal getCoinSupply() {
        return coinSupply;
    }

    public void setCoinSupply(BigDecimal coinSupply) {
        this.coinSupply = coinSupply;
    }

    public long getMasternodes() {
        return masternodes;
    }

    public void setMasternodes(long masternodes) {
        this.masternodes = masternodes;
    }

    public BigDecimal getMasternodeRoi() {
        return masternodeRoi;
    }

    public void setMasternodeRoi(BigDecimal masternodeRoi) {
        this.masternodeRoi = masternodeRoi;
    }

    public BigDecimal getMasternodeRoiDays() {
        return masternodeRoiDays;
    }

    public void setMasternodeRoiDays(BigDecimal masternodeRoiDays) {
        this.masternodeRoiDays = masternodeRoiDays;
    }
}
