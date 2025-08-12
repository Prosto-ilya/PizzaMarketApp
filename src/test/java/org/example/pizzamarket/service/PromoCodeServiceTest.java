package org.example.pizzamarket.service;

import org.example.pizzamarket.model.PromoCode;
import org.example.pizzamarket.repository.PromoCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromoCodeServiceTest {

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @InjectMocks
    private PromoCodeService promoCodeService;

    @Test
    void getAllPromoCodes_ReturnsAllPromoCodes() {

        PromoCode promo1 = new PromoCode();
        promo1.setCode("SUMMER20");
        PromoCode promo2 = new PromoCode();
        promo2.setCode("WINTER30");


        when(promoCodeRepository.findAll()).thenReturn(List.of(promo1, promo2));


        List<PromoCode> result = promoCodeService.getAllPromoCodes();


        assertEquals(2, result.size());
        assertEquals("SUMMER20", result.get(0).getCode());
        assertEquals("WINTER30", result.get(1).getCode());

        verify(promoCodeRepository, times(1)).findAll();
    }

    @Test
    void save_ValidPromoCode_ReturnsSavedPromoCode() {
        // Подготовка тестовых данных
        PromoCode promoToSave = new PromoCode();
        promoToSave.setCode("NEWYEAR25");

        PromoCode savedPromo = new PromoCode();
        savedPromo.setId(1L);
        savedPromo.setCode("NEWYEAR25");


        when(promoCodeRepository.save(promoToSave)).thenReturn(savedPromo);


        PromoCode result = promoCodeService.save(promoToSave);


        assertNotNull(result.getId());
        assertEquals("NEWYEAR25", result.getCode());

        verify(promoCodeRepository, times(1)).save(promoToSave);
    }

    @Test
    void deletePromoCode_ValidId_DeletesPromoCode() {

        promoCodeService.deletePromoCode(1L);


        verify(promoCodeRepository, times(1)).deleteById(1L);
    }

}