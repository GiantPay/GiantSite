package network.giantpay.dto;

public class PresaleRequestDto extends AbstractDto {

    private String email;
    private String gicAddress;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGicAddress() {
        return gicAddress;
    }

    public void setGicAddress(String gicAddress) {
        this.gicAddress = gicAddress;
    }

    @Override
    public String toString() {
        return "PresaleRequestDto{" +
                "email='" + email + '\'' +
                ", gicAddress='" + gicAddress + '\'' +
                '}';
    }
}
