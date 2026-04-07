package org.exalt.task2.security;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.entity.UserAccount;
import org.exalt.task2.repository.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username));

        // map UserRole → Spring GrantedAuthority
        // Spring Security expects roles prefixed with ROLE_
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + account.getRole().name());

        return User.builder()
                .username(account.getUsername())
                .password(account.getPasswordHash())
                .authorities(List.of(authority))
                .disabled(!account.isEnabled())
                .build();
    }
}