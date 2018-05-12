package network.giantpay.api.giant;

import network.giantpay.api.AbstractExplorer;
import network.giantpay.api.WalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GiantExplorer extends AbstractExplorer {

    @Autowired
    private Environment env;

    @PostConstruct
    public void createClient() throws WalletException {
        try {
            String url = env.getProperty("api.explorer.giant.url");

            this.initialize(url);
        } catch (Exception e) {
            throw new WalletException(e.getMessage(), e);
        }
    }
}
