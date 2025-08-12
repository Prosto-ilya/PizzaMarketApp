package org.example.pizzamarket.controller;

import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.service.OrderService;
import org.example.pizzamarket.service.PizzaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.*;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private PizzaService pizzaService;

    @Mock
    private Model model;


    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void showCreateOrderForm_ReturnsCreateOrderView() throws Exception {

        when(pizzaService.listProducts()).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"))
                .andExpect(model().attributeExists("pizzas"));
    }

    @Test
    void showAllOrders_ReturnsOrderListView() throws Exception {

        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderList"))
                .andExpect(model().attributeExists("orders"));
    }
    @Test
    void createOrder_ValidData_RedirectsToOrderList() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("customerName", "John");
        params.put("phoneNumber", "123456789");
        params.put("deliveryAddress", "Test Address");
        params.put("pizza_1", "2"); // 2 пиццы с id=1
        params.put("promoCode", "SUMMER20");

        // Выполнение запроса
        mockMvc.perform(post("/order/create")
                        .param("customerName", "John")
                        .param("phoneNumber", "123456789")
                        .param("deliveryAddress", "Test Address")
                        .param("pizza_1", "2")
                        .param("promoCode", "SUMMER20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order"));

        // Проверка бизнес-логики
        verify(orderService).createOrder(
                eq("John"),
                eq("123456789"),
                eq("Test Address"),
                eq(Map.of(1L, 2)),
                eq("SUMMER20")
        );
    }

    @Test
    void showOrderDetails_ReturnsOrderDetailsView() throws Exception {

        Order testOrder = new Order();
        when(orderService.getOrderById(1L)).thenReturn(testOrder);


        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderDetails"))
                .andExpect(model().attributeExists("order"));


    }
}
