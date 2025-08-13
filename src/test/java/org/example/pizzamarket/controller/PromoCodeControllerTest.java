package org.example.pizzamarket.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.example.pizzamarket.model.PromoCode;
import org.example.pizzamarket.service.impl.PromoCodeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.Collections;

class PromoCodeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PromoCodeServiceImpl promoCodeService;

    @InjectMocks
    private PromoCodeController promoCodeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(promoCodeController).build();
    }

    @Test
    void showAllPromoCodes() throws Exception {
        when(promoCodeService.getAllPromoCodes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/promo"))
                .andExpect(status().isOk())
                .andExpect(view().name("promoMenagement"))
                .andExpect(model().attributeExists("promoCodes"));
    }

    @Test
    void showCreateForm() throws Exception {
        mockMvc.perform(get("/admin/promo/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("promoForm"))
                .andExpect(model().attributeExists("promoCode"))
                .andExpect(model().attributeExists("discountTypes"));
    }

    @Test
    void createPromoCode() throws Exception {
        PromoCode promoCode = new PromoCode();
        promoCode.setId(1L);

        mockMvc.perform(post("/admin/promo/create")
                        .param("validFrom", LocalDateTime.now().toString())
                        .param("validTo", LocalDateTime.now().plusDays(1).toString())
                        .flashAttr("promoCode", promoCode))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/promo"));

        verify(promoCodeService).save(promoCode);
    }

    @Test
    void togglePromoCode() throws Exception {
        mockMvc.perform(post("/admin/promo/1/toggle"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/promo"));

        verify(promoCodeService).togglePromoCodeStatus(1L);
    }

    @Test
    void deletePromoCode() throws Exception {
        mockMvc.perform(post("/admin/promo/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/promo"));

        verify(promoCodeService).deletePromoCode(1L);
    }
}