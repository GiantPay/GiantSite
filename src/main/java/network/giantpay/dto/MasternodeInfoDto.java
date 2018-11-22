package network.giantpay.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MasternodeInfoDto {

    private BigDecimal daily;
    private BigDecimal monthly;
    private BigDecimal annual;
    private RoiDto roi;
    private BigDecimal days;
    private long masternodeCount;

}
