package network.giantpay.service;

import com.google.common.base.Strings;
import network.giantpay.dto.PresaleRequestDto;
import network.giantpay.dto.PresaleResponseDto;
import network.giantpay.dto.PresaleStatusResponseDto;
import network.giantpay.dto.TransactionDto;
import network.giantpay.error.ApiException;
import network.giantpay.model.PresaleRequest;
import network.giantpay.model.PresaleRequestStatus;
import network.giantpay.model.PresaleSetting;
import network.giantpay.repository.PresaleRequestRepository;
import network.giantpay.repository.PresaleSettingRepository;
import network.giantpay.api.WalletException;
import network.giantpay.api.bitcoin.BitcoinWallet;
import network.giantpay.api.giant.GiantWallet;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PresaleService {

    private final static Logger logger = LoggerFactory.getLogger(PresaleService.class);
    private final static int BAD_REQUEST = 400;
    private final static int FORBIDEN = 403;
    private final static int NOT_FOUND = 404;
    private final static Object requestSync = new Object();

    private PresaleRequestRepository presaleRequestRepository;
    private PresaleSettingRepository presaleSettingRepository;
    private BitcoinWallet bitcoinWallet;
    private GiantWallet giantWallet;

    @Value("${email.host}")
    private String emailHost;
    @Value("${email.port}")
    private int emailPort;
    @Value("${email.username}")
    private String emailUsername;
    @Value("${email.password}")
    private String emailPassword;

    @Autowired
    public PresaleService(PresaleRequestRepository presaleRequestRepository,
                          PresaleSettingRepository presaleSettingRepository,
                          BitcoinWallet bitcoinWallet,
                          GiantWallet giantWallet) {
        this.presaleRequestRepository = presaleRequestRepository;
        this.presaleSettingRepository = presaleSettingRepository;
        this.bitcoinWallet = bitcoinWallet;
        this.giantWallet = giantWallet;
    }

    public PresaleResponseDto createRequest(PresaleRequestDto presaleRequest) throws ApiException {

        logger.info("presale request received: {}", presaleRequest);
        try {

            if (Strings.isNullOrEmpty(presaleRequest.getEmail())) {
                throw new ApiException(BAD_REQUEST, "email", "Email is required");
            }
            if (!EmailValidator.getInstance().isValid(presaleRequest.getEmail())) {
                throw new ApiException(BAD_REQUEST, "email", "Email isn't valid");
            }
            if (!giantWallet.validateaddress(presaleRequest.getGicAddress())) {
                throw new ApiException(BAD_REQUEST, "gicAddress", "Giant address isn't valid");
            }

            PresaleSetting setting = presaleSettingRepository.findSetting();
            if (setting == null) {
                throw new ApiException(NOT_FOUND, "Unknown error, please try again later");
            }

            synchronized (requestSync) {
                BigDecimal gicBalance = giantWallet.getbalance();
                if (gicBalance.subtract(setting.getGicAmount()).doubleValue() < 0.01) {
                    throw new ApiException(FORBIDEN, "Presale is finished");
                }

                PresaleRequest presale = new PresaleRequest();
                presale.setCreatedAt(new Date());
                presale.setStatus(PresaleRequestStatus.INIT);
                presale.setGuid(UUID.randomUUID().toString());
                presale.setEmail(presaleRequest.getEmail());
                presale.setBtcAccount(bitcoinWallet.generateAccount());
                presale.setBtcAddress(bitcoinWallet.getnewaddress(presale.getBtcAccount()));
                presale.setBtcAmount(setting.getBtcPrice());
                presale.setGicAddress(presaleRequest.getGicAddress());
                presale.setGicAmount(setting.getGicAmount());
                presaleRequestRepository.save(presale);

                logger.info("presale request created: {}", presale);

                sendMessage("NEW PRESALE REQUEST", "ID: #" + presale.getId() + "\n" +
                        "guid: " + presale.getGuid() + "\n" +
                        "createdAt: " + presale.getCreatedAt() + "\n" +
                        "email: " + presale.getEmail() + "\n" +
                        "btc address: " + presale.getBtcAddress() + "\n" +
                        "btc account: " + presale.getBtcAccount() + "\n" +
                        "btc amount: " + presale.getBtcAmount() + "\n" +
                        "gic address: " + presale.getGicAddress() + "\n" +
                        "gic amount: " + presale.getGicAmount());

                return new PresaleResponseDto(presale);
            }
        } catch (WalletException e) {
            throw new ApiException(BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional
    @Scheduled(initialDelay = 60000, fixedRate = 10000)
    public void checkPayments() {
        presaleRequestRepository.findUnconfirmed().forEach(this::checkPayment);
    }

    private void checkPayment(PresaleRequest presale) {
        try {
            logger.info("checkPayment: {}", presale);

            List<TransactionDto> transactions = bitcoinWallet.listtransactions(presale.getBtcAccount());
            if (transactions != null && !transactions.isEmpty()) {
                Optional<TransactionDto> transaction = transactions.stream()
                        .filter(tx -> tx.getAmount().doubleValue() + 0.001 >= presale.getBtcAmount().doubleValue())
                        .findFirst();

                if (transaction.isPresent()) {
                    presale.setBtcConfirmations(transaction.get().getConfirmations());
                    if (presale.getBtcConfirmations() == 0) {
                        presale.setStatus(PresaleRequestStatus.UNCONFIRMED);
                        presale.setBtcTx(transaction.get().getTxid());

                        logger.info("unconfirmed tx {} for {}", transaction.get(), presale);
                    } else if (presale.getBtcConfirmations() >= 1) {
                        presale.setStatus(PresaleRequestStatus.CONFIRMED);
                        presale.setBtcTx(transaction.get().getTxid());
                        presale.setBtcTxAt(new Date());
                        presale.setGicTxAt(new Date());
                        presale.setGicTx(giantWallet.sendtoaddress(presale.getGicAddress(), presale.getGicAmount()));
                        presaleRequestRepository.save(presale);

                        logger.info("confirmed tx {} for {}", transaction.get(), presale);

                        sendMessage("CONFIRM PRESALE REQUEST", "ID: #" + presale.getId() + "\n" +
                                "guid: " + presale.getGuid() + "\n" +
                                "createdAt: " + presale.getCreatedAt() + "\n" +
                                "email: " + presale.getEmail() + "\n" +
                                "======\n" +
                                "btc tx: " + presale.getBtcTx() + "\n" +
                                "gic tx: " + presale.getGicTx() + "\n" +
                                "btc confirmations: " + presale.getBtcConfirmations() +
                                "======\n" +
                                "btc address: " + presale.getBtcAddress() + "\n" +
                                "btc account: " + presale.getBtcAccount() + "\n" +
                                "btc amount: " + presale.getBtcAmount() + "\n" +
                                "gic address: " + presale.getGicAddress() + "\n" +
                                "gic amount: " + presale.getGicAmount());
                    }

                } else {
                    logger.info("tx not found for {}", presale);
                }
            }
        } catch (WalletException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public PresaleStatusResponseDto getStatus(String guid) throws ApiException {
        return new PresaleStatusResponseDto(presaleRequestRepository.findByGuid(guid));
    }

    public void sendMessage(String subject, String text) {
        Thread senderThread = new Thread(() -> {
            try {
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                mailSender.setHost(emailHost);
                mailSender.setPort(emailPort);
                mailSender.setUsername(emailUsername);
                mailSender.setPassword(emailPassword);
                mailSender.getSession().setDebug(true);

                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.smtp.host", emailHost);
                props.put("mail.smtp.port", Integer.toString(emailPort));
                props.put("mail.smtp.socketFactory.port", Integer.toString(emailPort));
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.debug", "true");
                Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailUsername, emailPassword);
                    }
                });

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("noreply@giantpay.network");
                message.setTo("admin@giantpay.network");
                message.setSubject(subject);
                message.setText(text);
                mailSender.send(message);
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        senderThread.setName("sender-" + subject);
        senderThread.start();
    }
}
