package network.giantpay.service;

import com.google.common.base.Strings;
import network.giantpay.dto.UserDto;
import network.giantpay.model.User;
import network.giantpay.model.UserStatus;
import network.giantpay.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(prepareUsername(username));
        if (user == null) {
            user = userRepository.findByEmail(prepareEmail(username));
        }
        return user;
    }

    public void signUp(UserDto userDto, HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setStatus(UserStatus.ENABLED);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);

        // TODO add cookie

        logger.info("signUp: {}", user);
    }

    public boolean isCredentials(User profile, String password) {
        return passwordEncoder.matches(password, profile.getPassword());
    }

    static String prepareEmail(String email) {
        if (Strings.isNullOrEmpty(email)) {
            return null;
        }
        return email.replaceAll(" ", "").toLowerCase().trim();
    }

    static String prepareUsername(String email) {
        if (Strings.isNullOrEmpty(email)) {
            return null;
        }
        return email.replaceAll(" ", "").toLowerCase().trim();
    }
}


