package network.giantpay.config;

import lombok.AllArgsConstructor;
import network.giantpay.config.security.WebAuthenticationManager;
import network.giantpay.service.CredentialService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final CredentialService credentialService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/roadmap/*")
                .access("hasRole('USER')")
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();

        http.headers()
                .defaultsDisabled()
                .cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(this.userDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() {
        return new WebAuthenticationManager(this.userDetailsService, this.credentialService);
    }
}
