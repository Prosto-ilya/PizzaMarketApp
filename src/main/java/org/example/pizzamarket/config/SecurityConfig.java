package org.example.pizzamarket.config;

import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.service.impl.PhoneNumberUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PhoneNumberUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**",
                                "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers( "/pizza").permitAll()

                        // admin only
                        .requestMatchers("/pizza/add",
                                "/pizza/addForm",
                                "/pizza/orderList/**",
                                "/order/orderList/**",
                                "/admin/**",
                                "pizza/{id}/delete").hasRole("ADMIN")

                        // authenticated users
                        .requestMatchers("/order/**").authenticated()

                        // everything else needs login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("phoneNumber")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/pizza", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/pizza")
                        .permitAll()
                );
        return http.build();
    }

    // hook your PhoneNumberUserDetailsService + encoder into the AuthenticationManager
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

