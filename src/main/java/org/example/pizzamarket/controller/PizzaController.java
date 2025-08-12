package org.example.pizzamarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Pizza Management", description = "Endpoints for managing pizzas")
@Controller
@RequestMapping("/pizza")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;
    private final OrderService orderService;


    @Operation(summary = "List all pizzas")
    @GetMapping
    public String listPizzas(Model model) {
        model.addAttribute("pizzas", pizzaService.listProducts());
        return "list";
    }

    @Operation(summary = "Get pizza details")
    @GetMapping("/{id}")
    public String pizzaInfo(@PathVariable Long id, Model model) {
        model.addAttribute("pizza", pizzaService.getProductById(id));
        return "details";
    }

    @Operation(summary = "Get order page")
    @GetMapping("/order/{id}")
    public String showOrderPage(@PathVariable Long id, Model model) {
        model.addAttribute("pizza", pizzaService.getProductById(id));
        return "order";
    }

    @Operation(summary = "Page with create Order")
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

    @Operation(summary = "Page, that show form to create pizza")
    @GetMapping("/add")
    public String showAddPizzaForm(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "addForm";
    }

    @Operation(summary = "Page with save Pizza")
    @PostMapping("/add")
    public String savePizza(@ModelAttribute Pizza pizza, @RequestParam("file") MultipartFile file)
            throws IOException {
        pizzaService.savePizza(pizza, file);
        return "redirect:/pizza";
    }
}
