package com.antmen.antwork.admin;

import com.antmen.antwork.domain.user.service.AdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(AdminService.INITIAL_ADMIN_PASSWORD);
        System.out.println("{bcrypt}" + hashedPassword);
    }
}
