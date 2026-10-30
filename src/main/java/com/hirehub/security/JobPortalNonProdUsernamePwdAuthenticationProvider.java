package com.hirehub.security;

import com.hirehub.entity.HireHubUser;
import com.hirehub.repository.HireHubUserRepository;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("qa")
@Component
@Slf4j
@RequiredArgsConstructor
public class JobPortalNonProdUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final HireHubUserRepository hireHubUserRepository;

    @PostConstruct
    void warnThatPasswordlessLoginIsOn() {
        log.warn("QA PROFILE: passwordless login is ENABLED. Passwords are NOT checked.");
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        HireHubUser hireHubUser = hireHubUserRepository.findHireHubUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User details not found for the user: " + username));
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(hireHubUser.getRole().getName()));
        log.warn("Passwordless login used for user: {}", username);
        return new UsernamePasswordAuthenticationToken(hireHubUser, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
