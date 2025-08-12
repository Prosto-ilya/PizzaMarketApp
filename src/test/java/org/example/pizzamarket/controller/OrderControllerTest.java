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
    void showOrderDetails_ReturnsOrderDetailsView() throws Exception {

        Order testOrder = new Order();
        when(orderService.getOrderById(1L)).thenReturn(testOrder);


        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderDetails"))
                .andExpect(model().attributeExists("order"));


    }
}
