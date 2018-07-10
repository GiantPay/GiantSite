package network.giantpay.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "utm_sources")
public class UtmSource extends Identifiable {

    private String source;
    private String url;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
