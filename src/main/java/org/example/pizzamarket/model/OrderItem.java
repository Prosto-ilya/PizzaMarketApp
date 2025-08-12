package org.example.pizzamarket.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Позиция в заказе")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор позиции", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pizza_id", nullable = false)
    @Schema(description = "Пицца в заказе")
    private Pizza pizza;

    @Column(nullable = false)
    @Schema(description = "Количество", example = "2")
    private int quantity;

    @Column(name = "item_price", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Цена за единицу", example = "550.00")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(description = "Связанный заказ")
    private Order order;

    @Schema(description = "Общая стоимость позиции", example = "1100.00")
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}

