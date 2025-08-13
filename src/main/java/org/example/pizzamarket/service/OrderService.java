package org.example.pizzamarket.service;

import org.example.pizzamarket.model.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order createOrder(String customerName, String phoneNumber, String deliveryAddress, Map<Long, Integer> pizzaQuantities, String promoCode);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
    void deleteOrder(Long id);
}
