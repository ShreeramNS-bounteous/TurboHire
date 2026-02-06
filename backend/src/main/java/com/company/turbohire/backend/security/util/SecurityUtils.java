package com.company.turbohire.backend.security.util;

import com.company.turbohire.backend.security.model.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Long getCurrentUserId() {
        return ((AuthUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getUserId();
    }

    public static String getCurrentUserRole() {
        return ((AuthUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getRole();
    }

    public static Long getCurrentBU() {
        return ((AuthUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getBuId();
    }

}
