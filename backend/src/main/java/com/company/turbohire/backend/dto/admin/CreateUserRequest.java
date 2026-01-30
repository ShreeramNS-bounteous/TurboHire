package com.company.turbohire.backend.dto.admin;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String fullName;
    private String email;
    private String password;
}
