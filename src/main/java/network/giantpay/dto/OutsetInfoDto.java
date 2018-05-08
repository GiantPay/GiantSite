package network.giantpay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class OutsetInfoDto extends AbstractDto {

    private long height;
    @JsonProperty("total_amount")
    private BigDecimal coinSupply;

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public BigDecimal getCoinSupply() {
        return coinSupply;
    }

    public void setCoinSupply(BigDecimal coinSupply) {
        this.coinSupply = coinSupply;
    }
}
