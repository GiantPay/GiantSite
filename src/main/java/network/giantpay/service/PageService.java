package network.giantpay.service;

import com.google.common.base.Strings;
import com.google.common.primitives.Longs;
import network.giantpay.model.Page;
import network.giantpay.model.User;
import network.giantpay.repository.PageRepository;
import network.giantpay.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.List;

@Service
public class PageService {

    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CredentialService credentialService;

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

    public Page edit(MultiValueMap params) {
        String username = (String) params.getFirst("username");
        String password = (String) params.getFirst("password");
        User user = (User) userService.loadUserByUsername(username);
        if (user == null || !credentialService.apply(user, password)) {
            throw new RuntimeException();
        }

        Long id = Longs.tryParse((String) params.getFirst("id"));
        Page page = null;
        if (id != null) {
            page = pageRepository.findById(id).orElse(null);
        }

        if (page == null) {
            page = new Page();
            page.setCreatedAt(new Date());
            page.setCreatedBy(user);
        }

        page.setTitle((String) params.getFirst("title"));
        page.setAnnouncement((String) params.getFirst("announcement"));
        page.setText((String) params.getFirst("text"));
        page.setSeoTitle((String) params.getFirst("seoTitle"));
        page.setSeoDescription((String) params.getFirst("seoDescription"));
        page.setSeoKeywords((String) params.getFirst("seoKeywords"));
        page.setCategory((String) params.getFirst("category"));
        page.setTags(page.getSeoKeywords());
        page.setVisible(Boolean.parseBoolean((String) params.getFirst("visible")));
        page.setCoverUrl((String) params.getFirst("coverUrl"));
        page.setImages(ImageUtils.getImages(params));
        if (page.getId() == null) {
            page.setUrl("/" + page.getTitle()
                    .replaceAll("#", "")
                    .replaceAll("\\&", "")
                    .replaceAll("\\?", "")
                    .replaceAll("\\!", "")
                    .replaceAll("\\?", "")
                    .replaceAll(" ", "-")
                    .replaceAll(":", "-")
                    .replaceAll(";", "-")
                    .replaceAll("%", "-")
                    .replaceAll("'", "")
                    .replaceAll("\"", "")
                    .replaceAll("\\(", "")
                    .replaceAll("\\)", "")
                    .replaceAll("\\.", "")
                    .replaceAll("\\,", "")
                    .replaceAll("\\n", "-")
                    .replaceAll("--", "-")
                    .toLowerCase()
                    .trim()
            );
        }
        return pageRepository.save(page);
    }

    public List<Page> findLastPages() {
        return pageRepository.findFirst7ByVisibleTrueOrderByCreatedAtDesc();
    }
}
