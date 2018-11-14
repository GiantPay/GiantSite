package network.giantpay.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import network.giantpay.model.User;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Service
public class StorageService {

    private final String storagePath;

    private final UserDetailsService userService;

    private final CredentialService credentialService;

    public StorageService(final Environment environment,
                          final CredentialService credentialService,
                          final UserDetailsService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.storagePath = environment.getProperty("storage.path");
    }

    public Map<String, Object> uploadImage(String username, String password, MultipartFile file) {
        try {
            final User userDetails = (User) this.userService.loadUserByUsername(username);

            if (!this.credentialService.apply(userDetails, password)) {
                throw new UsernameNotFoundException("Passwords not matches");
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
