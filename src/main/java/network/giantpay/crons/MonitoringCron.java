package network.giantpay.crons;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class send health metrics to grafana
 * Enabled only if property 'monitoring.enabled' is setted to true ,default is false
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty("monitoring.enabled")
public final class MonitoringCron {

    /**
     * Datasource url
     */
    @Value("${monitoring.url}")
    private String monitoringUrl;

    /**
     * Datasource user
     */
    @Value("${monitoring.user}")
    private String monitoringUser;

    /**
     * Datasource password
     */
    @Value("${monitoring.password}")
    private String monitoringPassword;

    /**
     * List of urls to monitor
     */
    @Value("#{'${monitoring.urls}'.split(',')}")
    private List<String> servers;

    /**
     * Jdbc template
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Rest template
     */
    private final RestTemplate restTemplate;

    /**
     * Transaction template
     */
    private final TransactionTemplate transactionTemplate;

    /**
     * Init datasource
     */
    @PostConstruct
    private void init() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.monitoringUrl);
        hikariConfig.setUsername(this.monitoringUser);
        hikariConfig.setPassword(this.monitoringPassword);
        hikariConfig.setIdleTimeout(0);
        hikariConfig.setMaximumPoolSize(2);

        this.jdbcTemplate = new JdbcTemplate(new HikariDataSource(hikariConfig));

        log.info("Jdbc template for monitoring cron was configured");
    }

    /**
     * Check health of each server and save result to datasource
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void sendMetrics() {
        final List<Pair<String, Boolean>> responses = this.servers
                .parallelStream()
                .map(url -> new Pair<>(url, this.sendRequest(url)))
                .collect(Collectors.toList());

        this.saveMetrics(responses);
    }

    /**
     * @param responses List of pairs where key is server url and key is success representation
     */
    private void saveMetrics(final List<Pair<String, Boolean>> responses) {
        this.transactionTemplate.execute(transactionStatus -> {
            try {
                this.jdbcTemplate.batchUpdate(
                        "INSERT INTO monitoring (server_name,success,check_date) VALUES (?,?,now())",
                        new BatchPreparedStatementSetter() { // in batch
                            @Override
                            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                                final Pair<String, Boolean> response = responses.get(i);
                                ps.setString(1, serverName(response.getKey()));
                                ps.setBoolean(2, response.getValue());
                            }

                            @Override
                            public int getBatchSize() {
                                return responses.size();
                            }
                        });
                log.info("Successfully saved {} metrics", responses.size());
                return true;
            } catch (final Exception exc) {
                log.error("Can't save metrics", exc);
                transactionStatus.setRollbackOnly();
                return false;
            }
        });
    }

    /**
     * Send http request to given url
     *
     * @param url Url
     * @return True of status code is 200
     */
    private boolean sendRequest(final String url) {
        try {
            final ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
            return response.getStatusCodeValue() == 200;
        } catch (final Exception exc) {
            log.error("Can't send request to url {}. Most probably status code is not 200", url);
            return false;
        }
    }

    /**
     * @param url Fetch server name from url
     * @return Server name
     */
    private static String serverName(final String url) {
        if (url.contains("giantpay")) {
            return "giantpay";
        }
        if (url.contains("staker")) {
            return "stakertech";
        }
        if (url.contains("exchange")) {
            return "exchange";
        }
        throw new UnsupportedOperationException(String.format("Url %s not supported", url));
    }
}