package org.example.pizzamarket.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@Schema(description = "Пользователь системы")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Номер телефона", example = "+79123456789")
    private String phoneNumber;

    @Column(nullable = false)
    @Schema(description = "Зашифрованный пароль", hidden = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;

    @Schema(description = "Имя", example = "Иван")
    private String firstName;

    @Column(nullable = false)
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;

    @Column(nullable = false)
    @Schema(description = "Email", example = "user@example.com")
    private String email;

    @Schema(description = "Активен ли аккаунт", example = "true")
    private boolean enabled = true;

    @Schema(description = "Роли пользователей")
    public enum Role {
        @Schema(description = "Обычный пользователь")
        USER,

        @Schema(description = "Администратор")
        ADMIN
    }
}