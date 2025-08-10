package org.example.pizzamarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ingredients_id")
    private Long id;
    @Column(name = "ingredients_Name")
    private String ingredientsName;
    @Column(name = "price")
    private double price;
    @Column(name = "is_Vegetarian")
    private boolean isVegetarian;
    @Column(name = "is_Spicy")
    private boolean isSpicy;
}
