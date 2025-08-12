package org.example.pizzamarket.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "pizza")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Пицца")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pizza_id")
    @Schema(description = "Уникальный идентификатор пиццы", example = "1")
    private Long id;

    @Column(name = "pizza_name")
    @Schema(description = "Название пиццы", example = "Маргарита")
    private String pizzaName;

    @Column(name = "pizza_description", length = 1000)
    @Schema(description = "Описание пиццы", example = "Классическая пицца с томатным соусом, моцареллой и базиликом")
    private String pizzaDescription;

    @Column(name = "price", nullable = false)
    @Schema(description = "Цена", example = "550.00")
    private BigDecimal price;

    @Column(name = "is_available")
    @Schema(description = "Доступна ли для заказа", example = "true")
    private boolean available = true;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Schema(description = "Изображения пиццы")
    private List<Image> images = new ArrayList<>();

    @Schema(hidden = true)
    public void addImageToProduct(Image image) {
        image.setPizza(this);
        images.add(image);
    }
}
