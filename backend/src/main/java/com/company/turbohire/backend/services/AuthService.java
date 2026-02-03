package com.company.turbohire.backend.services;

import com.company.turbohire.backend.security.jwt.JwtTokenProvider;
import com.company.turbohire.backend.security.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String email, String password) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );

        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        return jwtTokenProvider.generateToken(
                authUser.getUserId(),
                authUser.getEmail(),
                authUser.getRole()
        );
    }
}
