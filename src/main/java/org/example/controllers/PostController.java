package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.request.PostRequest;
import org.example.dto.response.PostResponse;
import org.example.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<PostResponse> createPost(
            @PathVariable Long userId,
            @Valid @RequestBody PostRequest request) {
        return new ResponseEntity<>(postService.createPost(userId, request), HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponse> likePost(@PathVariable Long postId) {
        // Ahora devolvemos el post actualizado
        return ResponseEntity.ok(postService.giveLike(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build(); // Devuelve 204 como en image_fba586.png
    }
}