package org.example.pizzamarket.exeption;

public class PizzaNotFoundException extends RuntimeException {
    public PizzaNotFoundException(Long id) {
        super("Pizza with id " + id + " not found");
    }
}
