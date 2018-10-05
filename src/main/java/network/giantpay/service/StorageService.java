package network.giantpay.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import network.giantpay.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${storage.path}")
    private String storagePath;
    @Autowired
    private UserService userService;

    public Map<String, Object> uploadImage(String username, String password, MultipartFile file) {
        try {
            User user = (User) userService.loadUserByUsername(username);
            if (user == null || !userService.isCredentials(user, password)) {
                throw new RuntimeException();
            }

            String randomPath = getRandomString();
            File storageFileParent = new File(storagePath, randomPath);
            if (!storageFileParent.exists()) {
                storageFileParent.mkdirs();
            }
            file.transferTo(new File(storageFileParent, file.getOriginalFilename()));
            return ImmutableMap.of("ok", true, "url", "/i/" + randomPath + "/" + file.getOriginalFilename());
        } catch (Exception e) {
            return ImmutableMap.of("ok", false, "message", Strings.isNullOrEmpty(e.getMessage()) ? "Unknown error" : e.getMessage());
        }
    }

    private static String getRandomString() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .substring(0, 8)
                .toLowerCase();
    }
}
