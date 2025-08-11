package org.example.pizzamarket.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.Image;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.repository.PizzaRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



@Service
@RequiredArgsConstructor
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    @Transactional
    public List<Pizza> listProducts() {
        return pizzaRepository.findAll();
    }

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

    public Pizza getProductById(Long id) {
        return pizzaRepository.findById(id).orElseThrow();
    }
}