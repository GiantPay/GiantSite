package network.giantpay.api;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.URL;

public class AbstractExplorer {

    private final static Logger logger = LoggerFactory.getLogger(AbstractExplorer.class);

    private String url;

    public void initialize(String url) {
        this.url = url;
    }

    public BigDecimal getblockcount() {
        try {
            return new BigDecimal(IOUtils.toString(new URL(url + "/api/getblockcount"), "UTF-8"));
        } catch (Exception e) {
            if (logger.isTraceEnabled()) {
                logger.trace(e.getMessage(), e);
            }
            return null;
        }
    }

    public BigDecimal getdifficulty() {
        try {
            return new BigDecimal(IOUtils.toString(new URL(url + "/api/getdifficulty"), "UTF-8"));
        } catch (Exception e) {
            if (logger.isTraceEnabled()) {
                logger.trace(e.getMessage(), e);
            }
            return null;
        }
    }

    public BigDecimal getnetworkhashps() {
        try {
            return new BigDecimal(IOUtils.toString(new URL(url + "/api/getnetworkhashps"), "UTF-8"));
        } catch (Exception e) {
            if (logger.isTraceEnabled()) {
                logger.trace(e.getMessage(), e);
            }
            return null;
        }
    }
}
