package network.giantpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import static network.giantpay.dto.Dates.*;

@Data
@Builder
@AllArgsConstructor
public final class RoiDto {

    private static final int BLOCKS_PER_DAY = 720;

    private static final int COLLATERAL_PRICE = 1000;

    private final BigDecimal perYear;
    private final BigDecimal perMonth;
    private final BigDecimal perWeek;
    private final BigDecimal perDay;

    /**
     * Represent an empty {@link RoiDto}
     */
    public static RoiDto EMPTY = RoiDto.builder().perDay(BigDecimal.ZERO).perWeek(BigDecimal.ZERO).perMonth(BigDecimal.ZERO).perYear(BigDecimal.ZERO).build();

    /**
     * Create an instance of {@link RoiDto} using masterNodesAmount and masterNodeReward
     *
     * @param masterNodeReward  reward
     * @param masterNodesAmount amount
     * @return new {@link RoiDto} from given params
     */
    public static RoiDto fromMasterNodeReward(final BigDecimal masterNodeReward, final long masterNodesAmount) {

        final BigDecimal perYear = BigDecimal.valueOf(100 * BLOCKS_PER_DAY * DAYS_IN_YEAR)
                .multiply(masterNodeReward)
                .divide(BigDecimal.valueOf(COLLATERAL_PRICE * masterNodesAmount), 2, BigDecimal.ROUND_HALF_UP);
        final BigDecimal perMonth = BigDecimal.valueOf(100 * BLOCKS_PER_DAY * DAYS_IN_MONTH)
                .multiply(masterNodeReward)
                .divide(BigDecimal.valueOf(COLLATERAL_PRICE * masterNodesAmount), 2, BigDecimal.ROUND_HALF_UP);
        final BigDecimal perWeek = BigDecimal.valueOf(100 * BLOCKS_PER_DAY * DAYS_IN_WEEK)
                .multiply(masterNodeReward)
                .divide(BigDecimal.valueOf(COLLATERAL_PRICE * masterNodesAmount), 2, BigDecimal.ROUND_HALF_UP);
        final BigDecimal perDay = BigDecimal.valueOf(100 * BLOCKS_PER_DAY * ONE_DAY)
                .multiply(masterNodeReward)
                .divide(BigDecimal.valueOf(COLLATERAL_PRICE * masterNodesAmount), 2, BigDecimal.ROUND_HALF_UP);

        return new RoiDto(perYear, perMonth, perWeek, perDay);
    }
}
