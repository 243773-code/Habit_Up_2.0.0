package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AchievementRequest;
import org.example.dto.response.AchievementResponse;
import org.example.services.AchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    // Obtener todos los logros de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AchievementResponse>> getMyAchievements(@PathVariable Long userId) {
        return ResponseEntity.ok(achievementService.getAchievementsByUserId(userId));
    }

    // Obtener logros por categoría
    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<List<AchievementResponse>> getByCategory(
            @PathVariable Long userId,
            @PathVariable String category) {
        return ResponseEntity.ok(achievementService.getAchievementsByCategory(userId, category));
    }

    /**
     * ARREGLO: Cambiamos @RequestParam por @RequestBody AchievementRequest.
     * Ahora el controlador recibe el JSON completo de Insomnia.
     */
    @PostMapping("/user/{userId}/unlock")
    public ResponseEntity<AchievementResponse> unlockManual(
            @PathVariable Long userId,
            @Valid @RequestBody AchievementRequest request) { // 👈 Recibe el JSON completo
        return ResponseEntity.ok(achievementService.giveAchievement(userId, request));
    }
}