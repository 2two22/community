package twotwo.community.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twotwo.community.comment.service.AnswerCommentService;
import twotwo.community.comment.service.PostCommentService;
import twotwo.community.domain.PostType;
import twotwo.community.dto.request.CommentRequest;
import twotwo.community.dto.response.CommentResponse;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerCommentController {
    private final AnswerCommentService commentService;

    @PostMapping("/{answerId}/comments")
    public ResponseEntity<CommentResponse> create(@PathVariable String answerId, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @Valid @RequestBody CommentRequest form) {
        return ResponseEntity.ok(commentService.create(answerId, token, form.getContent()));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> update(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String commentId, @Valid @RequestBody CommentRequest form) {
        return ResponseEntity.ok(commentService.update(commentId, token, form.getContent()));
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> createReComment(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String commentId, @Valid @RequestBody CommentRequest form) {
        return ResponseEntity.ok(commentService.createReComment(commentId, token, form.getContent()));
    }


    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String commentId) {
        commentService.delete(commentId, token);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{answerId}/comments")
    public ResponseEntity<Slice<CommentResponse>> retrieve(@RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "10") int size,
                                                           @RequestParam(required = false) PostType postType,
                                                           @PathVariable String answerId,
                                                           @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(commentService.retrieveComments(answerId, token, page, size));
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> registerOrCancelLike(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String commentId) {
        commentService.registerOrCancelLike(commentId, token);
        return ResponseEntity.ok().build();
    }
}
