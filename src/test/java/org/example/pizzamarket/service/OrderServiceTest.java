package org.example.pizzamarket.service;

import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.model.PromoCode;
import org.example.pizzamarket.repository.OrderRepository;
import org.example.pizzamarket.repository.PizzaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private PromoCodeService promoCodeService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_NoPromoCode_Success() {
        // Подготовка данных
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setPrice(BigDecimal.valueOf(10));

        Map<Long, Integer> quantities = Map.of(1L, 2); // 2 пиццы

        // Настройка моков
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Вызов метода
        Order order = orderService.createOrder("Иван", "123", "ул. Тест", quantities, null);

        // Проверки
        assertEquals("Иван", order.getCustomerName());
        assertEquals(BigDecimal.valueOf(20), order.getTotalPrice()); // 2 x 10
        assertNull(order.getPromoCode());
    }


    @Test
    void createOrder_PizzaNotFound_ThrowsException() {
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder("Иван", "123", "ул. Тест", Map.of(1L, 1), null)
        );
    }
    @Test
    void getOrderById_Exists_ReturnsOrder() {
        Order expected = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(expected));

        Order result = orderService.getOrderById(1L);

        assertEquals(expected, result);
    }

    @Test
    void getOrderById_NotExists_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                orderService.getOrderById(1L)
        );
    }
    @Test
    void getAllOrders_ReturnsAll() {
        List<Order> expected = List.of(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(expected);

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
    }




}