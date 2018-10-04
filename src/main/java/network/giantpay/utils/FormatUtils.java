package network.giantpay.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    private final static DecimalFormat percentFormat;
    private final static DecimalFormat integerFormat;

    static {
        percentFormat = new DecimalFormat("#0.#");
        integerFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);

        DecimalFormatSymbols symbols = integerFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        integerFormat.setDecimalFormatSymbols(symbols);
    }

    public static String formatPercent(BigDecimal number) {
        return percentFormat.format(number);
    }

    public static String formatInteger(BigDecimal number) {
        return integerFormat.format(number.setScale(0, RoundingMode.HALF_DOWN));
    }
}
