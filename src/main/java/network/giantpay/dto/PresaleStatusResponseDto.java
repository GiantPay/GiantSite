package network.giantpay.dto;


import network.giantpay.model.PresaleRequest;

import java.math.BigDecimal;

public class PresaleStatusResponseDto extends AbstractDto {

    private String status;
    private BigDecimal btcAmount;
    private BigDecimal gicAmount;
    private String btcAddress;
    private String btcTx;
    private String gicTx;

    public PresaleStatusResponseDto(PresaleRequest presale) {
        if (presale != null) {
            this.status = presale.getStatus().toString();
            this.btcAmount = presale.getBtcAmount();
            this.gicAmount = presale.getGicAmount();
            this.btcAddress = presale.getBtcAddress();
            this.btcTx = presale.getBtcTx();
            this.gicTx = presale.getGicTx();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBtcAmount() {
        return btcAmount;
    }

    public void setBtcAmount(BigDecimal btcAmount) {
        this.btcAmount = btcAmount;
    }

    public BigDecimal getGicAmount() {
        return gicAmount;
    }

    public void setGicAmount(BigDecimal gicAmount) {
        this.gicAmount = gicAmount;
    }

    public String getBtcAddress() {
        return btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
    }

    public String getBtcTx() {
        return btcTx;
    }

    public void setBtcTx(String btcTx) {
        this.btcTx = btcTx;
    }

    public String getGicTx() {
        return gicTx;
    }

    public void setGicTx(String gicTx) {
        this.gicTx = gicTx;
    }
}
