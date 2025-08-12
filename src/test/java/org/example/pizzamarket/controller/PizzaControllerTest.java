package org.example.pizzamarket.controller;

import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.service.OrderService;
import org.example.pizzamarket.service.PizzaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class PizzaControllerTest {

    @Mock
    private PizzaService pizzaService;

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @InjectMocks
    private PizzaController pizzaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pizzaController).build();
    }
    @Test
    void listPizzas_ShouldReturnListView() throws Exception {

        when(pizzaService.listProducts()).thenReturn(List.of(new Pizza(), new Pizza()));


        mockMvc.perform(get("/pizza"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeExists("pizzas"));
    }
    @Test
    void pizzaInfo_ShouldReturnDetailsView() throws Exception {
        // Настройка тестовых данных
        Pizza testPizza = new Pizza();
        testPizza.setId(1L);


        when(pizzaService.getProductById(1L)).thenReturn(testPizza);


        mockMvc.perform(get("/pizza/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("details"))
                .andExpect(model().attributeExists("pizza"));
    }


    @Test
    void placeOrder_ShouldReturnSuccessView() throws Exception {

        Order testOrder = new Order();
        testOrder.setId(1L);


        when(orderService.createOrder(
                anyString(), anyString(), anyString(),
                anyMap(), anyString()))
                .thenReturn(testOrder);


        mockMvc.perform(post("/pizza/order/1")
                        .param("quantity", "2")
                        .param("customerName", "John")
                        .param("phoneNumber", "123456789")
                        .param("deliveryAddress", "Test Address")
                        .param("promoCode", "SUMMER20"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderSuccess"))
                .andExpect(model().attributeExists("order"));


    }
    @Test
    void showAddPizzaForm_ShouldReturnAddFormView() throws Exception {

        mockMvc.perform(get("/pizza/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addForm"))
                .andExpect(model().attributeExists("pizza"));
    }
    @Test
    void savePizza_ShouldRedirectToList() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );


        mockMvc.perform(multipart("/pizza/add")
                        .file(file)
                        .param("pizzaName", "New Pizza")
                        .param("price", "10.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pizza"));

    }
}
