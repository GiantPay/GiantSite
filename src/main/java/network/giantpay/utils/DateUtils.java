package network.giantpay.utils;

import network.giantpay.model.RoadMap;
import org.springframework.data.util.Pair;

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    /**
     * Ctor.
     */
    private DateUtils() {
        throw new UnsupportedOperationException("No instance of DateUtils for you");
    }

    /**
     * This method return two period to filter {@link RoadMap} from DB with 'WHERE' clause
     *
     * @param year      for date
     * @param monthFrom begin month
     * @param monthTo   end month
     * @return Pair where first is date from , second is date to
     */
    public static Pair<Date, Date> interval(final int year, final int monthFrom, final int monthTo) {
        final Calendar from = Calendar.getInstance();
        from.set(Calendar.YEAR, year);
        from.set(Calendar.MONTH, monthFrom);
        from.set(Calendar.DAY_OF_MONTH, 1);
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);

        final Calendar to = Calendar.getInstance();
        to.set(Calendar.YEAR, year);
        to.set(Calendar.MONTH, monthTo);
        to.set(Calendar.DAY_OF_MONTH, to.getActualMaximum(Calendar.DAY_OF_MONTH));
        from.set(Calendar.HOUR_OF_DAY, 23);
        from.set(Calendar.MINUTE, 59);
        from.set(Calendar.SECOND, 59);

        return Pair.of(from.getTime(), to.getTime());
    }
}
