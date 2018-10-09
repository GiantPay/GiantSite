package network.giantpay.repository;

import network.giantpay.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    List<Page> findFirst7ByVisibleTrueOrderByCreatedAtDesc();

    @Query(nativeQuery = true, value = "SELECT p.* FROM pages AS p WHERE p.visible AND upper(p.category) = upper(?) ORDER BY p.created_id DESC")
    List<Page> findAllByVisibleTrueAndCategoryOrderByCreatedAtDesc(String category);

    List<Page> findAllByVisibleTrueOrderByCreatedAtDesc();

    @Query(nativeQuery = true, value = "SELECT DISTINCT upper(p.category) FROM pages AS p WHERE visible ORDER BY 1")
    List<String> findCategories();

    Page findByUrl(String url);
}
