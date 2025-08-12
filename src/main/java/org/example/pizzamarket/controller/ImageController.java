package org.example.pizzamarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.Image;
import org.example.pizzamarket.repository.ImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "Image Management", description = "Управление изображениями")
public class ImageController {

    private final ImageRepository imageRepository;
    @Operation(
            summary = "Получить изображение по ID",
            description = "Возвращает изображение в виде массива байтов")
    @GetMapping("/images/{id}")
    @Transactional
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", image.getContentType())
                .body(image.getBytes());
    }
}



