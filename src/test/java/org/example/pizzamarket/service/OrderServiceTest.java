package org.example.pizzamarket.service;

import org.example.pizzamarket.exeption.OrderNotFoundException;
import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.repository.OrderRepository;
import org.example.pizzamarket.repository.PizzaRepository;
import org.example.pizzamarket.service.impl.OrderServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
    private OrderServiceImp orderService;

    @Test
    void createOrder_NoPromoCode_Success() {

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setPrice(BigDecimal.valueOf(10));

        Map<Long, Integer> quantities = Map.of(1L, 2); // 2 пиццы


        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));


        Order order = orderService.createOrder("Иван", "123", "ул. Тест", quantities, null);


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
    void getAllOrders_ReturnsAll() {
        List<Order> expected = List.of(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(expected);

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
    }


    @Test
    void deleteOrder_ShouldDeleteWhenOrderExists() {
        // 1. Подготовка моков
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        // 2. Вызов метода
        orderService.deleteOrder(1L);

        // 3. Проверки
        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void deleteOrder_ShouldThrowWhenOrderNotExists() {
        // 1. Подготовка моков
        when(orderRepository.existsById(1L)).thenReturn(false);

        // 2. Проверка исключения
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteOrder(1L);
        });

        // 3. Проверки
        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, never()).deleteById(any());
    }
}