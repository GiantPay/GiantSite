package network.giantpay.repository;

import network.giantpay.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    List<Page> findAllByVisibleTrueOrderByCreatedAtDesc();

    List<Page> findAllByVisibleTrueAndCategoryOrderByCreatedAtDesc(String category);

    @Query(nativeQuery = true, value = "SELECT c.name FROM " +
            "(SELECT p.category AS name, COUNT(*) AS count FROM pages AS p GROUP BY p.category) AS c " +
            "ORDER BY c.count")
    List<String> findCategories();

    Page findByUrl(String url);
}
