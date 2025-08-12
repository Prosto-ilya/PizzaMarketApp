package org.example.pizzamarket.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.service.OrderService;
import org.example.pizzamarket.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

import java.util.Map;


@Controller
@RequestMapping("/order")
@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PizzaService pizzaService;

    @Operation(summary = "Show order creation form")
    @GetMapping("/orderList")
    public String showAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orderList";
    }


    @Operation(summary = "Page, where create order")
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("pizzas", pizzaService.listProducts());
        return "createOrder";
    }
    @Operation(summary = "Create new order")
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

        Order order = orderService.createOrder(customerName, phoneNumber, deliveryAddress, pizzaQuantities, promoCode);

        return "redirect:/order/success/" + order.getId();
    }
    @Operation(summary = "Page, where show order details")
    @GetMapping("/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "orderDetails";
    }
    @Operation(summary = "Delete order")
    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/order/orderList";
    }

    @Operation(summary = "Show success page")
    @GetMapping("/success/{id}")
    public String showSuccessPage(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "orderSuccess";
    }
}