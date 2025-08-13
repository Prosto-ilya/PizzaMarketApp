package org.example.pizzamarket.service;

import org.example.pizzamarket.exeption.PizzaNotFoundException;
import org.example.pizzamarket.model.Image;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.repository.PizzaRepository;
import org.example.pizzamarket.service.impl.PizzaServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private PizzaServiceImp pizzaService;
    @Test
    void listProducts_ReturnsAllPizzas() {


        Pizza pizza1 = new Pizza();
        pizza1.setPizzaName("Margherita");
        Pizza pizza2 = new Pizza();
        pizza2.setPizzaName("Pepperoni");


        when(pizzaRepository.findAll()).thenReturn(List.of(pizza1, pizza2));


        List<Pizza> result = pizzaService.listProducts();


        assertEquals(2, result.size());
        assertEquals("Margherita", result.get(0).getPizzaName());
        assertEquals("Pepperoni", result.get(1).getPizzaName());


        verify(pizzaRepository, times(1)).findAll();
    }

    @Test
    void savePizza_WithoutImage_SavesSuccessfully() throws IOException {

        Pizza pizza = new Pizza();
        pizza.setPizzaName("Test Pizza");


        pizzaService.savePizza(pizza, null);


        verify(pizzaRepository, times(1)).save(pizza);
        assertTrue(pizza.getImages().isEmpty());
    }

    @Test
    void savePizza_WithImage_SavesWithImage() throws IOException {

        Pizza pizza = new Pizza();
        pizza.setPizzaName("Test Pizza");


        MultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );


        pizzaService.savePizza(pizza, file);


        verify(pizzaRepository, times(1)).save(pizza);
        assertEquals(1, pizza.getImages().size());

        Image image = pizza.getImages().iterator().next();
        assertEquals("image", image.getName());
        assertEquals("test.jpg", image.getOriginalFileName());
        assertEquals("image/jpeg", image.getContentType());
    }
    @Test
    void getProductById_ShouldReturnPizzaWhenExists() {
        Pizza expected = new Pizza();
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(expected));

        Pizza result = pizzaService.getProductById(1L);

        assertEquals(expected, result);
    }

    @Test
    void getProductById_ShouldThrowWhenNotExists() {
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PizzaNotFoundException.class, () -> {
            pizzaService.getProductById(1L);
        });
    }

}