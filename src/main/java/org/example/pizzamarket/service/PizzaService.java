package org.example.pizzamarket.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.Image;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.repository.ImageRepository;
import org.example.pizzamarket.repository.PizzaRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



@Service
@RequiredArgsConstructor
@Tag(name = "Pizza Service", description = "Сервис для работы с пиццами")
public class PizzaService {

    private final ImageRepository imageRepository;
    private final PizzaRepository pizzaRepository;
    @Operation(summary = "Получить список всех пицц",
            description = "Возвращает список всех доступных пицц")
    @Transactional
    public List<Pizza> listProducts() {
        return pizzaRepository.findAll();
    }
    @Operation(summary = "Сохранить пиццу",
            description = "Сохраняет пиццу с прикрепленным изображением")
    @Transactional
    public void savePizza(Pizza pizza, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            pizza.addImageToProduct(toImageEntity(file));
        }
        pizzaRepository.save(pizza);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
    @Operation(summary = "Получить пиццу по ID",
              description = "Возвращает пиццу с указанным идентификатором")
    public Pizza getProductById(Long id) {
        return pizzaRepository.findById(id).orElseThrow();
    }

}
