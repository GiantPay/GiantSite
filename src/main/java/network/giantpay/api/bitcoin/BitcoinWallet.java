package network.giantpay.api.bitcoin;

import network.giantpay.api.AbstractWallet;
import network.giantpay.api.WalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;

@Component
public class BitcoinWallet extends AbstractWallet {

    @Autowired
    private Environment env;

    @PostConstruct
    public void createClient() throws WalletException {
        try {
            URL url = new URL(env.getProperty("rpc.bitcoin.url"));
            String username = env.getProperty("rpc.bitcoin.user");
            String password = env.getProperty("rpc.bitcoin.password");

            this.initialize(url, username, password);
        } catch (Exception e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    @Override
    protected String getTicker() {
        return "BTC";
    }
}
