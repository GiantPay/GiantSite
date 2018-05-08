package network.giantpay.rpc;

public class WalletException extends Exception {

    public WalletException(String message) {
        super(message);
    }

    public WalletException(String message, Throwable e) {
        super(message, e);
    }
}
