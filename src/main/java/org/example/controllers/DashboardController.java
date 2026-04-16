package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.DashboardResponse;
import org.example.services.DashboardService;
import org.example.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor // 🔥 Esto inyecta automáticamente lo que sea 'private final'
@CrossOrigin(origins = "*")
public class DashboardController {

    // 1. Definimos los servicios como 'private final'
    private final DashboardService dashboardService;
    private final NotificationService notificationService;

    /**
     * 1. VISTA GENERAL
     */
    @GetMapping("/{userId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long userId) {
        return ResponseEntity.ok(dashboardService.getUserDashboard(userId));
    }

    /**
     * 2. CHECK-IN DIARIO
     */
    @PostMapping("/user/{userId}/check-in")
    public ResponseEntity<?> dailyCheckIn(@PathVariable Long userId) {
        dashboardService.processDailyCheckIn(userId);

        // Notificamos por WebSocket que el dashboard cambió (opcional pero pro)
        notificationService.sendDashboardUpdate(userId, "CHECK_IN", "¡Día completado!");

        return ResponseEntity.ok(Map.of("message", "¡Felicidades! Un día más a la cuenta."));
    }

    /**
     * 3. BOTÓN DE PÁNICO / SOS
     */
    @PostMapping("/user/{userId}/sos")
    public ResponseEntity<?> triggerSOS(@PathVariable Long userId) {
        dashboardService.handleSOS(userId);

        // 🔥 Ahora sí, notificationService ya no saldrá en rojo
        notificationService.sendSOSAlert(userId, "Usuario " + userId);

        return ResponseEntity.ok(Map.of("message", "Alerta enviada a tus contactos de apoyo. ¡Respira, no estás solo!"));
    }

    /**
     * 4. ESTADÍSTICAS DE AHORRO
     */
    @GetMapping("/user/{userId}/savings")
    public ResponseEntity<?> getSavings(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of("totalSaved", dashboardService.calculateTotalSavings(userId)));
    }

    /**
     * 5. RENDIMIENTO / PERFORMANCE
     */
    @GetMapping("/user/{userId}/performance")
    public ResponseEntity<Map<String, Object>> getUserPerformance(@PathVariable Long userId) {
        return ResponseEntity.ok(dashboardService.getUserPerformance(userId));
    }
}