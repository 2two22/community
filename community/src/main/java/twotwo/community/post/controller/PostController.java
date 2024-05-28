package twotwo.community.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<String> create(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @RequestPart PostRequest createPostRequest, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(postService.create(token, images, createPostRequest));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> update(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String postId, @RequestPart PostRequest updatePostRequest, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(postService.update(postId, images, updatePostRequest, token));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String postId) {
        postService.delete(postId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> retrieve(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String postId) {
        return ResponseEntity.ok(postService.retrieve(token, postId));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> retrieve(@RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size,
                                                       @RequestParam(required = false) PostType postType,
                                                       @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(postService.retrievePosts(token, page, size, postType));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> registerOrCancelLike(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String postId) {
        postService.registerOrCancelLike(postId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/count")
    public ResponseEntity<Long> getUsersPostCount(@PathVariable Long userId){
        return ResponseEntity.ok(postService.getUsersPostCount(userId));
    }
}
