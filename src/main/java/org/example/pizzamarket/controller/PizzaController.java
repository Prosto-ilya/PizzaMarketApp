package org.example.pizzamarket.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.dto.OrderItemDto;


import org.example.pizzamarket.model.Pizza;

import org.example.pizzamarket.service.OrderService;
import org.example.pizzamarket.service.PizzaService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import java.util.Map;

@Controller
@RequestMapping("/pizza")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;
    private final OrderService orderService;

    @GetMapping
    public String listPizzas(@RequestParam(required = false) String title, Model model) {
        List<Pizza> pizzas = pizzaService.listProducts(title);
        model.addAttribute("pizzas", pizzas);
        return "/list";
    }

    @GetMapping("/{id}")
    public String pizzaInfo(@PathVariable Long id, Model model) {
        Pizza pizza = pizzaService.getProductById(id);
        model.addAttribute("pizza", pizza);
        return "/details";
    }

    @GetMapping("/order/{id}")
    public String showOrderPage(@PathVariable Long id, Model model) {
        Pizza pizza = pizzaService.getProductById(id);
        model.addAttribute("pizza", pizza);
        return "/order";
    }

    @PostMapping("/order/{id}")
    public String placeOrder(
            @PathVariable Long id,
            @RequestParam int quantity,
            @RequestParam String customerName,
            @RequestParam String phoneNumber,
            @RequestParam String deliveryAddress,
            Model model
    ) {
        Map<Long, Integer> pizzaIdToQuantity = Map.of(id, quantity);
        var order = orderService.createOrder(customerName, phoneNumber, deliveryAddress, pizzaIdToQuantity);
        model.addAttribute("order", order);
        return "orderSuccess";
    }

    @GetMapping("/add")
    public String showAddPizzaForm(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "addForm";
    }

    @PostMapping("/add")
    public String savePizza(
            @ModelAttribute("pizza") Pizza pizza,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            pizzaService.savePizza(pizza, file);
            return "redirect:/pizza";
        } catch (IOException e) {
            return "errorPage";
        }
    }
}
