package org.example.pizzamarket.controller;

import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.repository.PizzaRepository;
import org.example.pizzamarket.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final PizzaRepository pizzaRepository;
    private final OrderService orderService;

    // Показать список всех заказов
    @GetMapping
    public String showAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orderList";
    }

    // Показать форму создания заказа
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        List<Pizza> pizzas = pizzaRepository.findAll();
        model.addAttribute("pizzas", pizzas);
        return "createOrder";
    }

    // Обработка формы заказа
    @PostMapping("/create")
    public String processOrder(
            @RequestParam String customerName,
            @RequestParam String phoneNumber,
            @RequestParam String deliveryAddress,
            @RequestParam Map<String, String> allParams
    ) {
        Map<Long, Integer> pizzaIdToQuantity = new HashMap<>();

        for (String key : allParams.keySet()) {
            if (key.startsWith("pizza_")) {
                Long pizzaId = Long.parseLong(key.substring(6));
                int quantity = Integer.parseInt(allParams.get(key));
                if (quantity > 0) {
                    pizzaIdToQuantity.put(pizzaId, quantity);
                }
            }
        }

        orderService.createOrder(customerName, phoneNumber, deliveryAddress, pizzaIdToQuantity);
        return "redirect:/order";
    }

    // Детали конкретного заказа
    @GetMapping("/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            model.addAttribute("errorMessage", "Заказ с ID " + id + " не найден.");
            return "errorPage";
        }
        model.addAttribute("order", order);
        return "orderDetails";
    }
}

