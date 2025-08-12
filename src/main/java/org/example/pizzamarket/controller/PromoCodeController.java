package org.example.pizzamarket.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Promo Code Management", description = "Управление промокодами")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @GetMapping
    public String showAllPromoCodes(Model model) {
        model.addAttribute("promoCodes", promoCodeService.getAllPromoCodes());
        return "promoMenagement";
    }

    @GetMapping("/create")
    @Operation(
            summary = "Показать форму создания промокода",
            description = "Возвращает HTML форму для создания нового промокода"
    )
    public String showCreateForm(Model model) {
        model.addAttribute("promoCode", new PromoCode());
        model.addAttribute("discountTypes", PromoCode.DiscountType.values());
        return "promoForm";
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создать новый промокод",
            description = "Создает новый промокод с указанными параметрами"
    )
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
    @Operation(
            summary = "Переключить статус промокода",
            description = "Активирует/деактивирует промокод по ID"
    )
    public String togglePromoCode(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        promoCodeService.togglePromoCodeStatus(id);
        redirectAttributes.addFlashAttribute("success", "Статус промокода изменен");
        return "redirect:/admin/promo";
    }

    @PostMapping("/{id}/delete")
    @Operation(
            summary = "Удалить промокод",
            description = "Удаляет промокод по ID"
    )
    public String deletePromoCode(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        promoCodeService.deletePromoCode(id);
        redirectAttributes.addFlashAttribute("success", "Промокод удален");
        return "redirect:/admin/promo";
    }
}