package network.giantpay.dto;

import java.math.BigDecimal;

public class MasternodeInfoDto {

    private BigDecimal daily;
    private BigDecimal monthly;
    private BigDecimal annual;
    private BigDecimal roi;
    private BigDecimal days;
    private long masternodeCount;

    public BigDecimal getDaily() {
        return daily;
    }

    public void setDaily(BigDecimal daily) {
        this.daily = daily;
    }

    public BigDecimal getMonthly() {
        return monthly;
    }

    public void setMonthly(BigDecimal monthly) {
        this.monthly = monthly;
    }

    public BigDecimal getAnnual() {
        return annual;
    }

    public void setAnnual(BigDecimal annual) {
        this.annual = annual;
    }

    public BigDecimal getRoi() {
        return roi;
    }

    public void setRoi(BigDecimal roi) {
        this.roi = roi;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public long getMasternodeCount() {
        return masternodeCount;
    }

    public void setMasternodeCount(long masternodeCount) {
        this.masternodeCount = masternodeCount;
    }
}
