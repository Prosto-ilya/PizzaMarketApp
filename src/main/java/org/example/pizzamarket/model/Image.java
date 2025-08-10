package org.example.pizzamarket.model;


import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "pizza_images")
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String originalFileName;
    private Long size;
    private String contentType;

    @Lob
    @Column(name = "bytes")
    private byte[] bytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;
}