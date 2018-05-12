package network.giantpay.dto;

import java.math.BigDecimal;

public class RateDto {

    private BigDecimal btc;
    private BigDecimal usd;

    public BigDecimal getBtc() {
        return btc;
    }

    public void setBtc(BigDecimal btc) {
        this.btc = btc;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }
}
