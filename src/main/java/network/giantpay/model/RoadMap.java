package network.giantpay.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Table(name = "roadmap")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RoadMap extends Identifiable {

    @Column(name = "event")
    private String event;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "ready_value")
    @Max(value = 100, message = "Percentage can't be more than 100")
    @Min(value = 0, message = "Percentage can't be less than 0")
    private int readyValue;

    @Transient
    private String color;

    @PostLoad
    private void withColor() {
        if (this.readyValue == 0) {
            this.color = "grey";
        } else if (this.readyValue == 100) {
            this.color = "dark";
        } else {
            this.color = "blue";
        }
    }

    public boolean isEmpty() {
        return this.readyValue == 0;
    }
}
