package twotwo.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import twotwo.community.dto.request.PostRequest;
import twotwo.community.dto.response.PostResponse;
import twotwo.community.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(Long userId, @RequestPart PostRequest form, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(postService.create(userId, form));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> update(Long userId, @PathVariable String postId, @RequestPart PostRequest form, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(postService.update(postId, userId, form));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponse> update(Long userId, @PathVariable String postId) {
        postService.delete(postId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> retrieve(@PathVariable String postId) {
        return ResponseEntity.ok(postService.retrieve(postId));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> retrieve() {
        return ResponseEntity.ok(postService.retrievePosts());
    }
}
