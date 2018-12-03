package network.giantpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GiantApplication {

    public static final String BASE_URL = "https://giantpay.network";

    public static void main(String[] args) {
        SpringApplication.run(GiantApplication.class, args);
    }
}
