package network.giantpay.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

import static network.giantpay.GiantApplication.BASE_URL;


@Component
@AllArgsConstructor
public class PagesUrlsRepository implements Supplier<List<String>> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<String> get() {
        return this.jdbcTemplate.queryForList("SELECT ? || url FROM pages;", new Object[]{BASE_URL + "/pages"}, String.class);
    }
}
