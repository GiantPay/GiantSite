package network.giantpay.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import network.giantpay.model.User;
import network.giantpay.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final String trimedName = removeEmptySpaces(username);

        return this.userRepository
                .findByUsernameOrEmail(trimedName, trimedName)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("No user with name %s", trimedName))
                );
    }


    private static String removeEmptySpaces(String str) {
        return Optional.ofNullable(str)
                .map(value -> value.replaceAll(" ", "").toLowerCase().trim())
                .get();
    }
}


