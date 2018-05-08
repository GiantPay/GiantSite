package network.giantpay.rpc;

import com.google.common.base.Splitter;
import com.google.common.primitives.Longs;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import network.giantpay.dto.MasternodeDto;
import network.giantpay.dto.OutsetInfoDto;
import network.giantpay.dto.TransactionDto;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractWallet {

    private JsonRpcHttpClient client;

    public void initialize(URL url, String username, String password) {
        String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", "Basic " + credentials);

        this.client = new JsonRpcHttpClient(url, headers);
    }

    public String generateAccount() {
        return (getTicker() + System.currentTimeMillis()).toLowerCase();
    }

    public boolean validateaddress(String address) throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            Map response = client.invoke("validateaddress", new Object[]{address}, Map.class);
            return (boolean) response.getOrDefault("isvalid", false);
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    public BigDecimal getbalance() throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            return new BigDecimal(client.invoke("getbalance", new Object[0], String.class));
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    public String getnewaddress(String address) throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            return client.invoke("getnewaddress", new Object[]{address}, String.class);
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    public List<TransactionDto> listtransactions(String account) throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            return (List<TransactionDto>) client.invoke("listtransactions", new Object[]{account}, List.class)
                    .stream()
                    .map(transaction -> {
                        Map transactionMap = (Map) transaction;

                        TransactionDto transactionDto = new TransactionDto();
                        transactionDto.setAccount((String) transactionMap.get("account"));
                        transactionDto.setAddress((String) transactionMap.get("address"));
                        transactionDto.setCategory((String) transactionMap.get("category"));
                        transactionDto.setAmount(new BigDecimal((double) transactionMap.get("amount")));
                        transactionDto.setVout((int) transactionMap.get("vout"));
                        transactionDto.setConfirmations((int) transactionMap.get("confirmations"));
                        transactionDto.setBlockhash((String) transactionMap.get("blockhash"));
                        if (transactionMap.get("blockindex") != null) {
                            transactionDto.setBlockindex((int) transactionMap.get("blockindex"));
                        }
                        transactionDto.setTxid((String) transactionMap.get("txid"));
                        return transactionDto;
                    })
                    .collect(Collectors.toList());
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    public String sendtoaddress(String address, BigDecimal amount) throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            return client.invoke("sendtoaddress", new Object[]{address, amount}, String.class);
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    protected abstract String getTicker();

    public long getblockcount() throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            return Longs.tryParse(client.invoke("getblockcount", new Object[0], String.class));
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    public OutsetInfoDto gettxoutsetinfo() throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            return client.invoke("gettxoutsetinfo", new Object[0], OutsetInfoDto.class);
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    public BigDecimal getnetworkhashps(long height) throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            return new BigDecimal(client.invoke("getnetworkhashps", new Object[]{height}, String.class));
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    public BigDecimal getdifficulty() throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            return new BigDecimal(client.invoke("getdifficulty", new Object[0], String.class));
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }

    public List<MasternodeDto> masternodelist() throws WalletException {
        if (client == null) {
            throw new WalletException("Wallet is not ready yet");
        }

        try {
            Map<String, String> masternodelist = client.invoke("masternodelist", new Object[]{"full"}, Map.class);
            List<MasternodeDto> masternodes = new ArrayList<>();

            masternodelist.forEach((key, value) -> {
                MasternodeDto masternode = new MasternodeDto();
                masternode.setTx(key);

                List<String> values = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(value);
                if (values != null && values.size() == 7) {
                    masternode.setStatus(values.get(0));
                    masternode.setProtocol(values.get(1));
                    masternode.setAddress(values.get(2));
                    masternode.setEndpoint(values.get(3));
                }
                masternodes.add(masternode);
            });

            return masternodes;
        } catch (Throwable e) {
            throw new WalletException(e.getMessage(), e);
        }
    }
}
