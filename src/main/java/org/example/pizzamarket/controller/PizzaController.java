package org.example.pizzamarket.controller;

import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.service.OrderService;
import org.example.pizzamarket.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/pizza")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;
    private final OrderService orderService;

    @GetMapping
    public String listPizzas( Model model) {
        model.addAttribute("pizzas", pizzaService.listProducts());
        return "list";
    }

    @GetMapping("/{id}")
    public String pizzaInfo(@PathVariable Long id, Model model) {
        model.addAttribute("pizza", pizzaService.getProductById(id));
        return "details";
    }

    @GetMapping("/order/{id}")
    public String showOrderPage(@PathVariable Long id, Model model) {
        model.addAttribute("pizza", pizzaService.getProductById(id));
        return "order";
    }

    @PostMapping("/order/{id}")
    public String placeOrder(
            @PathVariable Long id,
            @RequestParam int quantity,
            @RequestParam String customerName,
            @RequestParam String phoneNumber,
            @RequestParam String deliveryAddress,
            @RequestParam(required = false) String promoCode,
            Model model) {

        Map<Long, Integer> pizzaQuantities = Map.of(id, quantity);
        Order order = orderService.createOrder(customerName, phoneNumber, deliveryAddress, pizzaQuantities, promoCode);
        model.addAttribute("order", order);
        return "orderSuccess";
    }

    @GetMapping("/add")
    public String showAddPizzaForm(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "addForm";
    }

    @PostMapping("/add")
    public String savePizza(@ModelAttribute Pizza pizza, @RequestParam("file") MultipartFile file)
            throws IOException {
        pizzaService.savePizza(pizza, file);
        return "redirect:/pizza";
    }
}
