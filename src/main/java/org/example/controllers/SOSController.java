package org.example.controllers;

import org.example.services.SOSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador de Emergencia (Botón SOS).
 * Se comunica con el SOSService para disparar alertas vía WebSockets.
 */
@RestController
@RequestMapping("/api/sos")
@CrossOrigin(origins = "*")
public class SOSController {

    @Autowired
    private SOSService sosService;

    /**
     * DISPARAR ALERTA SOS
     * POST /api/sos/trigger/{userId}
     */
    @PostMapping("/trigger/{userId}")
    public ResponseEntity<Map<String, Object>> triggerSOS(@PathVariable Long userId) {
        // Llamamos al servicio que maneja la lógica y las notificaciones WS
        return ResponseEntity.ok(sosService.triggerSOS(userId));
    }

    /**
     * CANCELAR ALERTA
     * POST /api/sos/cancel/{userId}
     */
    @PostMapping("/cancel/{userId}")
    public ResponseEntity<String> cancelSOS(@PathVariable Long userId) {
        // Notificamos que la crisis pasó a través del servicio
        return ResponseEntity.ok(sosService.cancelSOS(userId));
    }
}