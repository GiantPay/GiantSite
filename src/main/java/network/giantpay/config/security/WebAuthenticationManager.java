package network.giantpay.config.security;

import lombok.AllArgsConstructor;
import network.giantpay.model.User;
import network.giantpay.service.CredentialService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class WebAuthenticationManager implements AuthenticationManager {

    private final UserDetailsService userService;

    private final CredentialService credentialService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User profile = (User) this.userService.loadUserByUsername(authentication.getPrincipal().toString());

        if (profile == null) {
            throw new UsernameNotFoundException(String.format("Unknown profile %s", authentication.getPrincipal()));
        }

        if (!profile.isEnabled()) {
            throw new DisabledException(String.format("Disabled profile %s", authentication.getPrincipal()));
        }

        if (!profile.isAccountNonLocked()) {
            throw new LockedException(String.format("Locked profile %s", authentication.getPrincipal()));
        }

        if (!this.credentialService.apply(profile, authentication.getCredentials().toString())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new UsernamePasswordAuthenticationToken(profile, null, profile.getAuthorities());
    }


}
