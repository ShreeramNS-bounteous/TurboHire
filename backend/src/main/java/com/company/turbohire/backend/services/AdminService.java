package com.company.turbohire.backend.services;

import com.company.turbohire.backend.dto.admin.CreateUserRequest;
import com.company.turbohire.backend.entity.Role;
import com.company.turbohire.backend.entity.User;
import com.company.turbohire.backend.notification.service.NotificationService;
import com.company.turbohire.backend.repository.RoleRepository;
import com.company.turbohire.backend.repository.UserRepository;
import com.company.turbohire.backend.security.password.PasswordEncoderConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;


    // 1️⃣ Create USER (Employee by default)
    public Long createUser(CreateUserRequest req) {

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(userRole)
                .status("ACTIVE")
                .build();

        userRepository.save(user);
        // 🔥 MAIL TO HR
        notificationService.sendHrCredentials(
                user,
                req.getPassword()
        );

        return user.getId();
    }

    // 2️⃣ Assign role (USER → RECRUITER)
    public void assignRole(Long userId, String roleName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);
        userRepository.save(user);
    }
}
