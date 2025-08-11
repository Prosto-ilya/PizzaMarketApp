package org.example.pizzamarket.controller;


import lombok.RequiredArgsConstructor;
import org.example.pizzamarket.model.PromoCode;
import org.example.pizzamarket.service.PromoCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/promo")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @GetMapping
    public String showAllPromoCodes(Model model) {
        model.addAttribute("promoCodes", promoCodeService.getAllPromoCodes());
        return "promoMenagement";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("promoCode", new PromoCode());
        model.addAttribute("discountTypes", PromoCode.DiscountType.values());
        return "promoForm";
    }

    @PostMapping("/create")
    public String createPromoCode(@ModelAttribute PromoCode promoCode,
                                  @RequestParam String validFrom,
                                  @RequestParam String validTo,
                                  RedirectAttributes redirectAttributes) {

        promoCode.setValidFrom(LocalDateTime.parse(validFrom));
        promoCode.setValidTo(LocalDateTime.parse(validTo));

        promoCodeService.save(promoCode);
        redirectAttributes.addFlashAttribute("success", "Промокод успешно создан!");
        return "redirect:/admin/promo";
    }

    @PostMapping("/{id}/toggle")
    public String togglePromoCode(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        promoCodeService.togglePromoCodeStatus(id);
        redirectAttributes.addFlashAttribute("success", "Статус промокода изменен");
        return "redirect:/admin/promo";
    }

    @PostMapping("/{id}/delete")
    public String deletePromoCode(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        promoCodeService.deletePromoCode(id);
        redirectAttributes.addFlashAttribute("success", "Промокод удален");
        return "redirect:/admin/promo";
    }
}