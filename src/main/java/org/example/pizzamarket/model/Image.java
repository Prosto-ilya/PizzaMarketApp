package org.example.pizzamarket.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "pizza_images")
@Data
@Schema(description = "Изображение пиццы")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор изображения", example = "1")
    private Long id;

    @Schema(description = "Название файла", example = "margherita.jpg")
    private String name;

    @Schema(description = "Оригинальное имя файла", example = "margherita-original.jpg")
    private String originalFileName;

    @Schema(description = "Размер файла в байтах", example = "102400")
    private Long size;

    @Schema(description = "MIME-тип содержимого", example = "image/jpeg")
    private String contentType;

    @Lob
    @Column(name = "bytes")
    @Schema(description = "Бинарные данные изображения", hidden = true)
    private byte[] bytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id")
    @Schema(description = "Связанная пицца")
    private Pizza pizza;
}