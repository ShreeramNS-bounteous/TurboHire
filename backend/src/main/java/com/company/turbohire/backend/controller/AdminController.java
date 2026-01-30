package com.company.turbohire.backend.controller;


import com.company.turbohire.backend.dto.admin.AssignRoleRequest;
import com.company.turbohire.backend.dto.admin.CreateUserRequest;
import com.company.turbohire.backend.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * Create User (Employee)
     */
    @PostMapping("/users")
    public Long createUser(
            @RequestBody CreateUserRequest request
    ) {
        return adminService.createUser(request);
    }

    /**
     * Assign Role to User (Make HR)
     */
    @PutMapping("/users/{userId}/assign-role")
    public void assignRole(
            @PathVariable Long userId,
            @RequestBody AssignRoleRequest request
    ) {
        adminService.assignRole(userId, request.getRoleName());
    }
}
