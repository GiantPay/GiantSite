package network.giantpay.utils;

import network.giantpay.dto.CoinInfoDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GiantUtils {

    public static BigDecimal getBlockReward(long height) {
        if (height <= 1) {
            return BigDecimal.valueOf(200000L);
        } else if (height <= 500) {
            return BigDecimal.ZERO;
        } else if (height <= 20000) {
            return BigDecimal.valueOf(10L);
        } else if (height <= 40000) {
            return BigDecimal.valueOf(15L);
        } else if (height <= 262800) {
            return BigDecimal.valueOf(20L);
        } else if (height <= 525600) {
            return BigDecimal.valueOf(10L);
        } else if (height <= 788400) {
            return BigDecimal.valueOf(8L);
        } else if (height <= 1051200) {
            return BigDecimal.valueOf(6L);
        } else if (height <= 1314000) {
            return BigDecimal.valueOf(4L);
        } else {
            return BigDecimal.valueOf(2L);
        }
    }

    public static BigDecimal getMasternodeReward(long height) {
        return getBlockReward(height).multiply(BigDecimal.valueOf(0.8));
    }

    public static BigDecimal getUsdVolume(CoinInfoDto btcInfo, BigDecimal volume) {
        if (btcInfo != null) {
            return volume.multiply(btcInfo.getPrice()).setScale(2, RoundingMode.HALF_DOWN);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
