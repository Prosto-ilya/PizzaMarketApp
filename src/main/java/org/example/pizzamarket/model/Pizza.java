package org.example.pizzamarket.model;

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
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pizza_id")
    private Long id;

    @Column(name = "pizza_name")
    private String pizzaName;

    @Column(name = "pizza_description", length = 1000)
    private String pizzaDescription;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "is_available")
    private boolean available = true;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    public void addImageToProduct(Image image) {
        image.setPizza(this);
        images.add(image);
    }
}
