package org.example.pizzamarket.service;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.example.pizzamarket.model.PromoCode;
import org.example.pizzamarket.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Tag(name = "Promo Code Service", description = "Сервис для работы с промокодами")
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    @Operation(summary = "Получить все промокоды",
            description = "Возвращает список всех промокодов")
    @Transactional
    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }
    @Operation(summary = "Сохранить промокод",
            description = "Создает или обновляет промокод")
    @Transactional
    public PromoCode save(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }
    @Operation(summary = "Переключить статус промокода",
            description = "Активирует/деактивирует промокод")
    @Transactional
    public void togglePromoCodeStatus(Long id) {
        PromoCode promo = promoCodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promo code not found"));
        promo.setActive(!promo.isActive());
    }
    @Operation(summary = "Удалить промокод",
            description = "Удаляет промокод с указанным идентификатором")
    @Transactional
    public void deletePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }
    @Operation(summary = "Найти промокод по коду",
            description = "Возвращает промокод с указанным кодом")
    public Optional<PromoCode> findByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }
}