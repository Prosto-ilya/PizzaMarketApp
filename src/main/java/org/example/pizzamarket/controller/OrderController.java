package org.example.pizzamarket.controller;


import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.service.OrderService;
import org.example.pizzamarket.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

import java.util.Map;


@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PizzaService pizzaService;

    @GetMapping
    public String showAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orderList";
    }

    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("pizzas", pizzaService.listProducts());
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrder(
            @RequestParam String customerName,
            @RequestParam String phoneNumber,
            @RequestParam String deliveryAddress,
            @RequestParam Map<String, String> allParams) {

        Map<Long, Integer> pizzaQuantities = new HashMap<>();
        String promoCode = allParams.get("promoCode");

        for (String key : allParams.keySet()) {
            if (key.startsWith("pizza_")) {
                Long pizzaId = Long.parseLong(key.substring(6));
                int quantity = Integer.parseInt(allParams.get(key));
                if (quantity > 0) {
                    pizzaQuantities.put(pizzaId, quantity);
                }
            }
        }

        orderService.createOrder(customerName, phoneNumber, deliveryAddress, pizzaQuantities, promoCode);
        return "redirect:/order";
    }

    @GetMapping("/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "orderDetails";
    }
}