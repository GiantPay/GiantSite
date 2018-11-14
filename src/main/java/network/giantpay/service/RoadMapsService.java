package network.giantpay.service;

import lombok.AllArgsConstructor;
import network.giantpay.model.RoadMap;
import network.giantpay.repository.AvailableYearsRepository;
import network.giantpay.repository.RoadMapRepository;
import network.giantpay.utils.DateUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class RoadMapsService {

    private final static String Q1 = "Q1";

    private final static String Q2 = "Q2";

    private final static String Q3 = "Q3";

    private final static String Q4 = "Q4";

    private final AvailableYearsRepository availableYearsRepository;

    private final RoadMapRepository roadMapRepository;

    /**
     * @return a map of roadMapsByYear where
     * key = year
     * value = [
     * key = quarter(Q1,Q2,Q3,Q4),
     * value = {@link List} of {@link RoadMap} in given quarter
     * ]
     * Example:
     * key = 2018,
     * value = [
     * Q1 :[
     * {
     * RoadMap1...
     * }
     * ],
     * Q2 :[]
     * <p>
     * ]
     */
    public Map<Integer, Map<String, List<RoadMap>>> roadMapsByYear() {
        final List<Integer> years = this.availableYearsRepository.get();
        final Map<Integer, Map<String, List<RoadMap>>> result = new HashMap<>(4);
        final List<RoadMap> all = this.roadMapRepository.findAll();
        for (final int year : years) {
            final Map<String, List<RoadMap>> quarters = new HashMap<>(10);

            final Pair<Date, Date> q1Interval = DateUtils.interval(year, Calendar.JANUARY, Calendar.MARCH);
            quarters.put(Q1, filterByInterval(all, q1Interval));

            final Pair<Date, Date> q2Interval = DateUtils.interval(year, Calendar.APRIL, Calendar.JUNE);
            quarters.put(Q2, filterByInterval(all, q2Interval));

            final Pair<Date, Date> q3Interval = DateUtils.interval(year, Calendar.JULY, Calendar.SEPTEMBER);
            quarters.put(Q3, filterByInterval(all, q3Interval));

            final Pair<Date, Date> q4Interval = DateUtils.interval(year, Calendar.OCTOBER, Calendar.DECEMBER);
            quarters.put(Q4, filterByInterval(all, q4Interval));

            result.put(year, quarters);
        }
        return result;
    }

    /**
     * @return list of RoadMaps
     */
    public List<RoadMap> roadMaps() {
        return this.roadMapRepository.findAllByOrderByDateAsc();
    }

    /**
     * @param origin   RoadMaps
     * @param interval interval to filter using from , to dates
     * @return RoadMaps in given interval
     */
    private static List<RoadMap> filterByInterval(final List<RoadMap> origin, final Pair<Date, Date> interval) {
        return origin.stream()
                .filter(roadmap -> roadmap.getDate().after(interval.getFirst()) && roadmap.getDate().before(interval.getSecond()))
                .collect(toList());
    }
}
