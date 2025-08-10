package org.example.pizzamarket.repository;

import org.example.pizzamarket.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    List<Pizza> findByPizzaNameContainingIgnoreCase(String pizzaName);
}
