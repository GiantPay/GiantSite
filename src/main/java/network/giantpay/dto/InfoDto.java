package network.giantpay.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class InfoDto extends AbstractDto {

    private BigDecimal rate;
    private BigDecimal changePrice24h;
    private BigDecimal volume;
    private BigDecimal usdVolume;
    private BigDecimal changeVolume24h;
    private long height;
    private BigDecimal reward;
    private BigDecimal networkHashrate;
    private BigDecimal networkDifficulty;
    private BigDecimal coinSupply;
    private long masternodes;
    private BigDecimal masternodeRoi;
    private BigDecimal masternodeRoiDays;

}
