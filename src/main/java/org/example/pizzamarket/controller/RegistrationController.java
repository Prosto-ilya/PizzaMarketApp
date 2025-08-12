package org.example.pizzamarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.User;
import org.example.pizzamarket.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @Operation(
            summary = "Показать форму регистрации",
            description = "Возвращает HTML страницу с формой регистрации")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping
    @Operation(
            summary = "Зарегистрировать нового пользователя",
            description = "Создает учетную запись нового пользователя"
    )
    public String registerUser(@ModelAttribute("user") User user, BindingResult result) {
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            result.rejectValue("phoneNumber", "error.user", "Этот номер телефона уже зарегистрирован");
            return "registration";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        userRepository.save(user);

        return "redirect:/login";
    }
}
