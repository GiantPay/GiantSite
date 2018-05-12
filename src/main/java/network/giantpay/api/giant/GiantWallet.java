package network.giantpay.api.giant;

import network.giantpay.api.AbstractWallet;
import network.giantpay.api.WalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;

@Component
public class GiantWallet extends AbstractWallet {

    @Autowired
    private Environment env;

    @PostConstruct
    public void createClient() throws WalletException {
        try {
            URL url = new URL(env.getProperty("rpc.giant.url"));
            String username = env.getProperty("rpc.giant.user");
            String password = env.getProperty("rpc.giant.password");

            this.initialize(url, username, password);
        } catch (Exception e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    @Override
    protected String getTicker() {
        return "GIC";
    }
}
