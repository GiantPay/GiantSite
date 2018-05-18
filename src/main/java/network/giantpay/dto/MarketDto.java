package network.giantpay.dto;

import java.math.BigDecimal;

public class MarketDto extends AbstractDto {

    private BigDecimal buy;
    private BigDecimal sell;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal last;
    private BigDecimal volumeGic;
    private BigDecimal volumeBtc;
    private BigDecimal change;

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal sell) {
        this.sell = sell;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public BigDecimal getVolumeGic() {
        return volumeGic;
    }

    public void setVolumeGic(BigDecimal volumeGic) {
        this.volumeGic = volumeGic;
    }

    public BigDecimal getVolumeBtc() {
        return volumeBtc;
    }

    public void setVolumeBtc(BigDecimal volumeBtc) {
        this.volumeBtc = volumeBtc;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    @Override
    public String toString() {
        return "MarketDto{" +
                "buy=" + buy +
                ", sell=" + sell +
                ", low=" + low +
                ", high=" + high +
                ", last=" + last +
                ", volumeGic=" + volumeGic +
                ", volumeBtc=" + volumeBtc +
                ", change=" + change +
                '}';
    }
}
