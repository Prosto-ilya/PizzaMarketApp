package org.example.pizzamarket.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.example.pizzamarket.model.PromoCode;
import org.example.pizzamarket.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    @Transactional
    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    @Transactional
    public PromoCode save(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }

    @Transactional
    public void togglePromoCodeStatus(Long id) {
        PromoCode promo = promoCodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promo code not found"));
        promo.setActive(!promo.isActive());
    }

    @Transactional
    public void deletePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }

    public Optional<PromoCode> findByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }
}