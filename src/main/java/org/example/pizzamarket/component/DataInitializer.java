package org.example.pizzamarket.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.User;
import org.example.pizzamarket.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        try {
            String adminPhone = "+375000000000";
            if (!userRepository.existsByPhoneNumber(adminPhone)) {
                User admin = new User();
                admin.setPhoneNumber(adminPhone);
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(User.Role.ADMIN);
                admin.setFirstName("Admin");
                admin.setLastName("Adminov");
                admin.setEmail("admin@gmail.com");// Обязательное поле
                admin.setEnabled(true);

                userRepository.save(admin);
                System.out.println("Admin created successfully!");
            }
        } catch (Exception e) {
            System.err.println("Failed to create admin user:");
            e.printStackTrace();
        }
    }
}