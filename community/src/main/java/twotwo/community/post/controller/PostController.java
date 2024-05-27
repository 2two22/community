package twotwo.community.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import twotwo.community.domain.PostType;
import twotwo.community.dto.request.PostRequest;
import twotwo.community.dto.response.PostResponse;
import twotwo.community.post.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> create(Long userId, @RequestPart PostRequest form, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(postService.create(userId, images, form));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> update(Long userId, @PathVariable String postId, @RequestPart PostRequest form, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(postService.update(postId, images, form, userId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(Long userId, @PathVariable String postId) {
        postService.delete(postId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> retrieve(Long userId, @PathVariable String postId) {
        return ResponseEntity.ok(postService.retrieve(userId, postId));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> retrieve(@RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size,
                                                       @RequestParam(required = false) PostType postType,
                                                       Long userId) {
        return ResponseEntity.ok(postService.retrievePosts(userId, page, size, postType));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> registerOrCancelLike(Long userId, @PathVariable String postId) {
        postService.registerOrCancelLike(postId, userId);
        return ResponseEntity.ok().build();
    }
}
