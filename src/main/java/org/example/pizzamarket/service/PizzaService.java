package org.example.pizzamarket.service;

import org.example.pizzamarket.model.Pizza;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface PizzaService {
    List<Pizza> listProducts();
    Pizza getProductById(Long id);
    void savePizza(Pizza pizza, MultipartFile file) throws IOException;
}