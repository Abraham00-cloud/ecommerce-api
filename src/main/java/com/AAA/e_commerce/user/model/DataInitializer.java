package com.AAA.e_commerce.user.model;

import com.AAA.e_commerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setRole(Role.ADMIN);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));

                userRepository.save(admin);
                System.out.println("default admin created");
            } else {
                System.out.println("admin already exists");
            }
        };
    }
}
