package org.example.pizzamarket.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.Image;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.repository.ImageRepository;
import org.example.pizzamarket.service.PizzaService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/pizza")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;

    @GetMapping
    public String listPizzas(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("pizzas", pizzaService.listProducts(title));
        return "list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "addForm";
    }

    @PostMapping("/add")
    public String createPizza(@ModelAttribute Pizza pizza,
                              @RequestParam("file") MultipartFile file) throws IOException {
        pizzaService.savePizza(pizza, file);
        return "redirect:/pizza";
    }

    @GetMapping("/{id}")
    @Transactional
    public String pizzaInfo(@PathVariable Long id, Model model) {
        Pizza pizza = pizzaService.getProductById(id);
        Hibernate.initialize(pizza.getImages());
        model.addAttribute("pizza", pizza);
        model.addAttribute("images", pizza.getImages());
        return "details";
    }
}

