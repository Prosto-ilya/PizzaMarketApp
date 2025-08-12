package org.example.pizzamarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Authentication", description = "Аутентификация пользователей")
public class LoginController {

    @GetMapping("/login")
    @Operation(
            summary = "Показать форму входа",
            description = "Возвращает HTML страницу с формой входа"
    )
    public String showLoginForm() {
        return "login";
    }
}

