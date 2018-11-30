package network.giantpay;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@SpringBootApplication
@EnableScheduling
public class GiantApplication implements CommandLineRunner {

    public static final String BASE_URL = "https://giantpay.network";

    public static void main(String[] args) {
        SpringApplication.run(GiantApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
       /* try{
            WebSitemapGenerator sitemapGenerator = WebSitemapGenerator.builder("https://giantpay.network/", new File(("/home/strogiyotec/site")))
                    .build();
            sitemapGenerator.addUrl("https://giantpay.network/team");
            sitemapGenerator.write();
        }catch (final Exception exc){
            exc.printStackTrace();
        }*/

    }

}
