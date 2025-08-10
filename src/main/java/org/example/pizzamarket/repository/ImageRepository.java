package org.example.pizzamarket.repository;

import org.example.pizzamarket.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
