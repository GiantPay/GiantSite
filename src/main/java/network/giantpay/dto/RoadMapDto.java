package network.giantpay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import network.giantpay.model.RoadMap;

import java.util.List;

@AllArgsConstructor
@Data
public final class RoadMapDto {

    private final int year;

    private final Quarter quarter;

    private final List<RoadMap> roadMaps;

    public String yearWithQuarter() {
        return String.format("%d %s", this.year, this.quarter.value());
    }
}
