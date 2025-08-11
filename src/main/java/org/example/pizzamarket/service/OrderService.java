package org.example.pizzamarket.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.model.OrderItem;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.model.PromoCode;
import org.example.pizzamarket.repository.OrderRepository;
import org.example.pizzamarket.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PizzaRepository pizzaRepository;
    private final PromoCodeService promoCodeService;


    @Transactional
    public Order createOrder(String customerName,
                             String phoneNumber,
                             String deliveryAddress,
                             Map<Long, Integer> pizzaQuantities,
                             String promoCode) {

        // 1. Создаем заказ
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setPhoneNumber(phoneNumber);
        order.setDeliveryAddress(deliveryAddress);
        order.setOrderTime(LocalDateTime.now());

        // 2. Добавляем пиццы
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : pizzaQuantities.entrySet()) {
            if (entry.getValue() <= 0) continue;

            Pizza pizza = pizzaRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Пицца не найдена"));

            // 3. Создаем позицию заказа (обязательно устанавливаем цену!)
            OrderItem item = new OrderItem();
            item.setPizza(pizza);
            item.setQuantity(entry.getValue());
            item.setPrice(pizza.getPrice()); // <- Вот ключевая строка!
            item.setOrder(order);

            order.getItems().add(item);
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // 4. Применяем промокод
        if (promoCode != null && !promoCode.isEmpty()) {
            Optional<PromoCode> promo = promoCodeService.findByCode(promoCode);
            if (promo.isPresent() && promo.get().isValid()) {
                order.setPromoCode(promo.get());
                total = applyDiscount(total, promo.get());
            }
        }

        order.setTotalPrice(total);
        return orderRepository.save(order);
    }

    private BigDecimal applyDiscount(BigDecimal total, PromoCode promo) {
        if (promo.getDiscountType() == PromoCode.DiscountType.PERCENTAGE) {
            BigDecimal discount = total.multiply(promo.getDiscountValue().divide(BigDecimal.valueOf(100)));
            return total.subtract(discount);
        } else {
            return total.subtract(promo.getDiscountValue()).max(BigDecimal.ZERO);
        }
    }



    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
    }

    @Transactional
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}