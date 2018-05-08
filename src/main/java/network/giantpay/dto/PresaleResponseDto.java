package network.giantpay.dto;

import network.giantpay.model.PresaleRequest;

import java.math.BigDecimal;

public class PresaleResponseDto extends AbstractDto {

    private String guid;
    private BigDecimal amount;
    private String address;

    public PresaleResponseDto() {
    }

    public PresaleResponseDto(PresaleRequest presale) {
        this.guid = presale.getGuid();
        this.amount = presale.getBtcAmount();
        this.address = presale.getBtcAddress();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
