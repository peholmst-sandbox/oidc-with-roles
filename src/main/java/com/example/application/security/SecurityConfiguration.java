package com.example.application.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Set;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll());

        // Icons from the line-awesome addon
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll());
        super.configure(http);
        http.oauth2Login(oauth2Login -> {
            oauth2Login.userInfoEndpoint(userInfoEndpoint ->
                    userInfoEndpoint.oidcUserService(customUserService()));
        });
    }

    @Bean
    OidcUserService customUserService() {
        var service = new OidcUserService();
        service.setOidcUserMapper((request, userInfo) -> new DefaultOidcUser(getGrantedAuthoritiesForUser(request.getIdToken().getSubject()), request.getIdToken(), userInfo));
        return service;
    }

    private Set<GrantedAuthority> getGrantedAuthoritiesForUser(String principalName) {
        // Replace this with your own role lookup.
        return Set.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_" + principalName));
    }
}
