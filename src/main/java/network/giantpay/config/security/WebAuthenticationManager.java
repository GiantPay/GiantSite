package network.giantpay.config.security;

import network.giantpay.model.User;
import network.giantpay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class WebAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User profile = (User) userService.loadUserByUsername(authentication.getPrincipal().toString());

        if (profile == null) {
            throw new UsernameNotFoundException(String.format("Unknown profile %s", authentication.getPrincipal()));
        }

        if (!profile.isEnabled()) {
            throw new DisabledException(String.format("Disabled profile %s", authentication.getPrincipal()));
        }

        if (!profile.isAccountNonLocked()) {
            throw new LockedException(String.format("Locked profile %s", authentication.getPrincipal()));
        }

        if (!userService.isCredentials(profile, authentication.getCredentials().toString())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new UsernamePasswordAuthenticationToken(profile, null, profile.getAuthorities());
    }
}
