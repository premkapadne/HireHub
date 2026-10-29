package com.hirehub.security;

import com.hirehub.entity.HireHubUser;
import com.hirehub.repository.HireHubUserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("!prod")
@Component
@RequiredArgsConstructor
public class JobPortalNonProdUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final HireHubUserRepository hireHubUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        HireHubUser hireHubUser = hireHubUserRepository.findHireHubUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User details not found for the user: " + username)
                );
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(hireHubUser.getRole().getName()));
        return new UsernamePasswordAuthenticationToken(hireHubUser, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
