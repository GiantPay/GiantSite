package network.giantpay.service;

import lombok.AllArgsConstructor;
import network.giantpay.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

/**
 * This class check credentials of {@link User}
 * with given password
 */
@Service
@AllArgsConstructor
public class CredentialService implements BiFunction<User, String, Boolean> {

    private final PasswordEncoder encoder;



    @Override
    public Boolean apply(final User user, final String password) {
        return this.encoder.matches(password, user.getPassword());
    }
}
