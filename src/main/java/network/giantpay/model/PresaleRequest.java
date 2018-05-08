package network.giantpay.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "presale_requests")
public class PresaleRequest extends Identifiable {

    private String guid;
    @Enumerated(EnumType.STRING)
    private PresaleRequestStatus status;
    private String email;
    @Column(name = "btc_address")
    private String btcAddress;
    @Column(name = "btc_account")
    private String btcAccount;
    @Column(name = "btc_tx")
    private String btcTx;
    @Column(name = "gic_address")
    private String gicAddress;
    @Column(name = "gic_tx")
    private String gicTx;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "finished_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;
    @Column(name = "btc_tx_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date btcTxAt;
    @Column(name = "btc_confirmations")
    private int btcConfirmations;
    @Column(name = "gic_tx_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date gicTxAt;
    @Column(name = "btc_amount")
    private BigDecimal btcAmount;
    @Column(name = "gic_amount")
    private BigDecimal gicAmount;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public PresaleRequestStatus getStatus() {
        return status;
    }

    public void setStatus(PresaleRequestStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBtcAddress() {
        return btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
    }

    public String getBtcAccount() {
        return btcAccount;
    }

    public void setBtcAccount(String btcAccount) {
        this.btcAccount = btcAccount;
    }

    public String getBtcTx() {
        return btcTx;
    }

    public void setBtcTx(String btcTx) {
        this.btcTx = btcTx;
    }

    public String getGicAddress() {
        return gicAddress;
    }

    public void setGicAddress(String gicAddress) {
        this.gicAddress = gicAddress;
    }

    public String getGicTx() {
        return gicTx;
    }

    public void setGicTx(String gicTx) {
        this.gicTx = gicTx;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Date getBtcTxAt() {
        return btcTxAt;
    }

    public void setBtcTxAt(Date btcTxAt) {
        this.btcTxAt = btcTxAt;
    }

    public Date getGicTxAt() {
        return gicTxAt;
    }

    public void setGicTxAt(Date gicTxAt) {
        this.gicTxAt = gicTxAt;
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

    public int getBtcConfirmations() {
        return btcConfirmations;
    }

    public void setBtcConfirmations(int btcConfirmations) {
        this.btcConfirmations = btcConfirmations;
    }

    @Override
    public String toString() {
        return "PresaleRequest{" +
                "id='" + getId() + '\'' +
                ", guid='" + guid + '\'' +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", gicAddress='" + gicAddress + '\'' +
                ", btcAccount=" + btcAccount + '\'' +
                ", btcAddress=" + btcAddress + '\'' +
                ", createdAt=" + createdAt +
                ", finishedAt=" + finishedAt +
                ", btcAmount=" + btcAmount +
                ", btcConfirmations= " + btcConfirmations +
                ", gicAmount=" + gicAmount +
                '}';
    }
}
