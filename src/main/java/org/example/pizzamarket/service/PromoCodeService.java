package org.example.pizzamarket.service;


import org.example.pizzamarket.model.PromoCode;

import java.util.List;
import java.util.Optional;

public interface PromoCodeService {
    List<PromoCode> getAllPromoCodes();
    PromoCode save(PromoCode promoCode);
    void togglePromoCodeStatus(Long id);
    void deletePromoCode(Long id);
    Optional<PromoCode> findByCode(String code);
}
