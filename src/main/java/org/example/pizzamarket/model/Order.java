package org.example.pizzamarket.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String phoneNumber;
    private String deliveryAddress;
    private LocalDateTime orderTime;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne
    private PromoCode promoCode;

    // Метод для добавления позиции
    public void addItem(OrderItem item) {
        if (item == null) return;

        item.setOrder(this);
        items.add(item);
        totalPrice = totalPrice.add(item.getTotalPrice());
    }
}


