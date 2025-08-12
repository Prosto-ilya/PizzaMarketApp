package org.example.pizzamarket.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@Data
@NoArgsConstructor
@Schema(description = "Промокод")
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор промокода", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Код промокода", example = "SUMMER2023")
    private String code;

    @Schema(description = "Описание промокода", example = "Летняя скидка 15%")
    private String description;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Тип скидки", example = "PERCENTAGE")
    private DiscountType discountType;

    @Schema(description = "Значение скидки", example = "3.00")
    private BigDecimal discountValue;

    @Schema(description = "Дата начала действия", example = "2023-06-01T00:00:00")
    private LocalDateTime validFrom;

    @Schema(description = "Дата окончания действия", example = "2023-08-31T23:59:59")
    private LocalDateTime validTo;

    @Schema(description = "Активен ли промокод", example = "true")
    private boolean active = true;

    @Schema(description = "Типы скидок")
    public enum DiscountType {
        @Schema(description = "Процентная скидка")
        PERCENTAGE,

        @Schema(description = "Фиксированная сумма")
        FIXED_AMOUNT
    }

    @Schema(description = "Проверяет валидность промокода")
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return active &&
                !now.isBefore(validFrom) &&
                !now.isAfter(validTo);
    }
}
