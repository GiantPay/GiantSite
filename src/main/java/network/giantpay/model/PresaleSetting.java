package network.giantpay.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "presale_settings")
public class PresaleSetting extends Identifiable {

    private boolean enabled;
    @Column(name = "btc_price")
    private BigDecimal btcPrice;
    @Column(name = "gic_amount")
    private BigDecimal gicAmount;
    @Column(name = "awaiting_time")
    private int awaitingTime;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public BigDecimal getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(BigDecimal btcPrice) {
        this.btcPrice = btcPrice;
    }

    public BigDecimal getGicAmount() {
        return gicAmount;
    }

    public void setGicAmount(BigDecimal gicAmount) {
        this.gicAmount = gicAmount;
    }

    public int getAwaitingTime() {
        return awaitingTime;
    }

    public void setAwaitingTime(int awaitingTime) {
        this.awaitingTime = awaitingTime;
    }
}
