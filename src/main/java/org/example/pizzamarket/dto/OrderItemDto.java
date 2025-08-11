package org.example.pizzamarket.dto;

import lombok.*;
@Getter
@Data
@Setter
public class OrderItemDto {
    private Long pizzaId;
    private int quantity;

    public OrderItemDto(Long pizzaId, int quantity) {
        this.pizzaId = pizzaId;
        this.quantity = quantity;
    }

}

