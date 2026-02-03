package com.company.turbohire.backend.security.services;

import com.company.turbohire.backend.entity.User;
import com.company.turbohire.backend.repository.UserRepository;
import com.company.turbohire.backend.security.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new AuthUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),   // REQUIRED
                user.getRole().getName()
        );
    }
}
