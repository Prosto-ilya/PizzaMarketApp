package org.example.pizzamarket.service.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.User;
import org.example.pizzamarket.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Tag(name = "User Details Service", description = "Сервис аутентификации по номеру телефона")
public class PhoneNumberUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


    @Operation(summary = "Загрузить пользователя по номеру телефона")
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phoneNumber));


        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhoneNumber()) // Используем phoneNumber как username
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isEnabled())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}