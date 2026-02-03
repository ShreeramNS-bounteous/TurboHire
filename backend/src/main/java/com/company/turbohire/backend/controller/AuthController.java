package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.loginDto.LoginRequest;
import com.company.turbohire.backend.dto.loginDto.LoginResponse;
import com.company.turbohire.backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * LOGIN API
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        String token = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return new LoginResponse(token);
    }
}
