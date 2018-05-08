package network.giantpay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

//@Configuration
public class EmailConfig {

    @Value("${email.host}")
    private String emailHost;
    @Value("${email.port}")
    private int emailPort;
    @Value("${email.username}")
    private String emailUsername;
    @Value("${email.password}")
    private String emailPassword;

    @Bean
    public JavaMailSender getJavaMailSender() {
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
        return mailSender;
    }

}
