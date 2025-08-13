package org.example.pizzamarket.service.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pizzamarket.exeption.OrderNotFoundException;
import org.example.pizzamarket.model.Order;
import org.example.pizzamarket.model.OrderItem;
import org.example.pizzamarket.model.Pizza;
import org.example.pizzamarket.model.PromoCode;
import org.example.pizzamarket.repository.OrderRepository;
import org.example.pizzamarket.repository.PizzaRepository;
import org.example.pizzamarket.service.OrderService;
import org.example.pizzamarket.service.PromoCodeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Tag(name = "Order Service", description = "Сервис для работы с заказами")
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final PizzaRepository pizzaRepository;
    private final PromoCodeService promoCodeService;


    @Transactional
    @Operation(summary = "Создать новый заказ",
            description = "Создает заказ с указанными данными клиента и списком пицц")
    public Order createOrder(String customerName,
                             String phoneNumber,
                             String deliveryAddress,
                             Map<Long, Integer> pizzaQuantities,
                             String promoCode) {

        //  Создаем заказ
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setPhoneNumber(phoneNumber);
        order.setDeliveryAddress(deliveryAddress);
        order.setOrderTime(LocalDateTime.now());

        //  Добавляем в эотот заказ пиццы
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : pizzaQuantities.entrySet()) {
            if (entry.getValue() <= 0) continue;

            Pizza pizza = pizzaRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Пицца не найдена"));

            //  Создаем позицию заказа с обязательно указанной ценой, иначе будеь error
            OrderItem item = new OrderItem();
            item.setPizza(pizza);
            item.setQuantity(entry.getValue());
            item.setItem_price(pizza.getPrice());
            item.setOrder(order);

            order.getItems().add(item);
            total = total.add(item.getItem_price().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        //  Просмотр и применение промокода
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


    @Operation(summary = "Получить заказ по ID",
            description = "Возвращает заказ с указанным идентификатором")
    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order not found with id: {}", id);
                    return new OrderNotFoundException(id);
                });
    }

    @Operation(summary = "Получить все заказы",
            description = "Возвращает список всех заказов")
    @Transactional
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    @Operation(summary = "Удалить заказ",
            description = "Удаляет заказ с указанным идентификатором")
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }
}