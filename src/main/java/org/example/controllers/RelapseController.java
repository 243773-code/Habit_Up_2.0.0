package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.request.RelapseRequest;
import org.example.dto.response.RelapseResponse;
import org.example.services.RelapseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relapses")
@CrossOrigin(origins = "*") // Para que Alex se conecte desde cualquier puerto de React
public class RelapseController {

    @Autowired
    private RelapseService relapseService;

    /**
     * 1. POST /api/relapses/user/{userId}
     * Registrar un nuevo tropiezo (Vista de Emergencia/Botón Pánico).
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<RelapseResponse> createRelapse(
            @PathVariable Long userId,
            @Valid @RequestBody RelapseRequest request) {

        RelapseResponse response = relapseService.createRelapse(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 2. GET /api/relapses/user/{userId}
     * Obtener el historial completo (Vista 16 - Timeline).
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RelapseResponse>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(relapseService.getRelapseHistory(userId));
    }

    /**
     * 3. GET /api/relapses/user/{userId}/stats
     * Obtener datos agrupados para Gráficas (Pie/Bar Charts).
     * Devuelve: [ ["Ansiedad", 5], ["Presión Social", 2] ]
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<List<Object[]>> getStats(@PathVariable Long userId) {
        return ResponseEntity.ok(relapseService.getRelapseStatsByReason(userId));
    }

    /**
     * 4. DELETE /api/relapses/{id}
     * Por si Alex quiere borrar un registro erróneo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelapse(@PathVariable Long id) {
        relapseService.deleteRelapse(id);
        return ResponseEntity.noContent().build();
    }
}