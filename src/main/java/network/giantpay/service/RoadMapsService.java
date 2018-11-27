package network.giantpay.service;

import lombok.AllArgsConstructor;
import network.giantpay.dto.RoadMapDto;
import network.giantpay.model.RoadMap;
import network.giantpay.repository.AvailableYearsRepository;
import network.giantpay.repository.RoadMapRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static network.giantpay.dto.Quarter.*;
import static network.giantpay.utils.DateUtils.interval;

@Service
@AllArgsConstructor
public class RoadMapsService {

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
        final List<RoadMap> roadMaps = this.roadMapRepository.findAll();

        final Map<Integer, Map<String, List<RoadMap>>> response = new HashMap<>(4);

        for (final int year : years) {
            final Map<String, List<RoadMap>> quarters = new HashMap<>(10);

            final Pair<Date, Date> q1Interval = interval(year, Calendar.JANUARY, Calendar.MARCH);
            quarters.put(Q1.value(), filterByInterval(roadMaps, q1Interval));

            final Pair<Date, Date> q2Interval = interval(year, Calendar.APRIL, Calendar.JUNE);
            quarters.put(Q2.value(), filterByInterval(roadMaps, q2Interval));

            final Pair<Date, Date> q3Interval = interval(year, Calendar.JULY, Calendar.SEPTEMBER);
            quarters.put(Q3.value(), filterByInterval(roadMaps, q3Interval));

            final Pair<Date, Date> q4Interval = interval(year, Calendar.OCTOBER, Calendar.DECEMBER);
            quarters.put(Q4.value(), filterByInterval(roadMaps, q4Interval));

            response.put(year, quarters);
        }
        return response;
    }
    //// TODO: 11/28/18 rewrite this logic
    public List<RoadMapDto> quarteredRoadMaps() {
        final List<Integer> years = this.availableYearsRepository.get();
        final List<RoadMap> roadMaps = this.roadMapRepository.findAll();

        final List<RoadMapDto> response = new ArrayList<>(4);

        for (final int year : years) {
            final List<RoadMap> q1RoadMaps = filterByInterval(roadMaps, interval(year, Calendar.JANUARY, Calendar.MARCH));

            final List<RoadMap> q2RoadMaps = filterByInterval(roadMaps, interval(year, Calendar.APRIL, Calendar.JUNE));

            final List<RoadMap> q3RoadMaps = filterByInterval(roadMaps, interval(year, Calendar.JULY, Calendar.SEPTEMBER));

            final List<RoadMap> q4RoadMaps = filterByInterval(roadMaps, interval(year, Calendar.OCTOBER, Calendar.DECEMBER));

            if (!q1RoadMaps.isEmpty()) {
                response.add(new RoadMapDto(year, Q1, q1RoadMaps));
            }
            if (!q2RoadMaps.isEmpty()) {
                response.add(new RoadMapDto(year, Q2, q2RoadMaps));
            }
            if (!q3RoadMaps.isEmpty()) {
                response.add(new RoadMapDto(year, Q3, q3RoadMaps));
            }
            if (!q4RoadMaps.isEmpty()) {
                response.add(new RoadMapDto(year, Q4, q4RoadMaps));
            }
        }
        return response;
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
