package twotwo.community.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import twotwo.community.comment.service.PostCommentService;
import twotwo.community.domain.PostType;
import twotwo.community.dto.request.CommentRequest;
import twotwo.community.dto.request.PostRequest;
import twotwo.community.dto.response.CommentResponse;
import twotwo.community.dto.response.PostResponse;
import twotwo.community.post.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostCommentController {
    private final PostCommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> create(@PathVariable String postId, Long userId, @Valid @RequestBody CommentRequest form) {
        return ResponseEntity.ok(commentService.create(postId, userId, form.getContent()));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> update(Long userId, @PathVariable String commentId, @Valid @RequestBody CommentRequest form) {
        return ResponseEntity.ok(commentService.update(commentId, userId, form.getContent()));
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> createReComment(Long userId, @PathVariable String commentId, @Valid @RequestBody CommentRequest form) {
        return ResponseEntity.ok(commentService.createReComment(commentId, userId, form.getContent()));
    }


    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(Long userId, @PathVariable String commentId) {
        commentService.delete(commentId, userId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{postId}/comments")
    public ResponseEntity<Slice<CommentResponse>> retrieve(@RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "10") int size,
                                                           @RequestParam(required = false) PostType postType,
                                                           @PathVariable String postId,
                                                           Long userId) {
        return ResponseEntity.ok(commentService.retrieveComments(postId, userId, page, size));
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> registerOrCancelLike(Long userId, @PathVariable String commentId) {
        commentService.registerOrCancelLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
