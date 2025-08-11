package org.example.pizzamarket.service;


import lombok.RequiredArgsConstructor;


import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.model.OrderItem;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.repository.OrderRepository;
import org.example.pizzamarket.repository.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PizzaService pizzaService;
    private final PizzaRepository pizzaRepository;

    @Transactional
    public Order createOrder(String name, String phone, String address, Map<Long, Integer> pizzaIdToQuantity) {
        Order order = new Order();
        order.setCustomerName(name);
        order.setPhoneNumber(phone);
        order.setDeliveryAddress(address);
        order.setOrderTime(LocalDateTime.now());

        for (Map.Entry<Long, Integer> entry : pizzaIdToQuantity.entrySet()) {
            Pizza pizza = pizzaRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Пицца не найдена: " + entry.getKey()));
            OrderItem item = new OrderItem(pizza, entry.getValue());
            order.addItem(item);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
