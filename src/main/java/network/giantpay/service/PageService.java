package network.giantpay.service;

import com.google.common.base.Strings;
import network.giantpay.model.Page;
import network.giantpay.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    @Autowired
    private PageRepository pageRepository;

    public List<Page> findAll(String category) {
        if (Strings.isNullOrEmpty(category)) {
            return pageRepository.findAllByVisibleTrueOrderByCreatedAtDesc();
        } else {
            return pageRepository.findAllByVisibleTrueAndCategoryOrderByCreatedAtDesc(category);
        }
    }

    public List<String> findCategories() {
        return pageRepository.findCategories();
    }

    public Page getPage(String url) {
        return pageRepository.findByUrl(url.trim());
    }
}
