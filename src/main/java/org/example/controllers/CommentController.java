package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @PathVariable Long userId,
            @RequestBody Map<String, String> body) {

        commentService.addComment(postId, userId, body.get("content"));
        return ResponseEntity.ok(Map.of("message", "Comentario publicado"));
    }
}