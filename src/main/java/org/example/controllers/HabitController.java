package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.request.HabitRequest;
import org.example.dto.request.RelapseRequest;
import org.example.dto.response.HabitResponse;
import org.example.services.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = "*")
public class HabitController {

    @Autowired
    private HabitService habitService;

    // 🔥 FIJATE BIEN: La URL correcta es /api/habits/user/2
    @PostMapping("/user/{userId}")
    public ResponseEntity<HabitResponse> createHabit(@PathVariable Long userId, @Valid @RequestBody HabitRequest request) {
        return new ResponseEntity<>(habitService.createHabit(userId, request), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitResponse>> getMyHabits(@PathVariable Long userId) {
        return ResponseEntity.ok(habitService.getActiveHabits(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitResponse> getHabitById(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.getHabitById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> updateHabit(@PathVariable Long id, @Valid @RequestBody HabitRequest request) {
        return ResponseEntity.ok(habitService.updateHabit(id, request));
    }

    @PostMapping("/{id}/relapse")
    public ResponseEntity<HabitResponse> registerRelapse(@PathVariable Long id, @RequestBody RelapseRequest request) {
        return ResponseEntity.ok(habitService.processRelapse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }
}