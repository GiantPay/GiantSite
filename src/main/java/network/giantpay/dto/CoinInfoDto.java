package network.giantpay.dto;

import java.math.BigDecimal;

public class CoinInfoDto {

    private String name;
    private String ticker;
    private BigDecimal price;
    private BigDecimal change;
    private BigDecimal volume;
    private BigDecimal supply;

    public CoinInfoDto() {

    }

    public CoinInfoDto(String name, String ticker, BigDecimal price, BigDecimal change, BigDecimal volume, BigDecimal supply) {
        this.name = name;
        this.ticker = ticker;
        this.price = price;
        this.change = change;
        this.volume = volume;
        this.supply = supply;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getSupply() {
        return supply;
    }

    public void setSupply(BigDecimal supply) {
        this.supply = supply;
    }

    @Override
    public String toString() {
        return "CoinInfoDto{" +
                "name='" + name + '\'' +
                ", ticker='" + ticker + '\'' +
                ", price=" + price +
                ", change=" + change +
                ", volume=" + volume +
                ", supply=" + supply +
                '}';
    }
}
