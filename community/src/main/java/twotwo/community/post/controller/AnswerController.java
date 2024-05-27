package twotwo.community.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import twotwo.community.dto.request.AnswerRequest;
import twotwo.community.dto.response.AnswerResponse;
import twotwo.community.dto.response.PostResponse;
import twotwo.community.post.service.AnswerService;

import java.util.List;

@RestController
@RequestMapping("/posts/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<String> create(Long userId, @RequestPart AnswerRequest form, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(answerService.create(userId, images, form));
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<String> update(Long userId, @PathVariable String answerId, @RequestPart AnswerRequest form, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(answerService.update(answerId, images, form, userId));
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> delete(Long userId, @PathVariable String answerId) {
        answerService.delete(answerId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{answerId}/pin")
    public ResponseEntity<Void> pinnedAnswer(Long userId, @PathVariable String answerId) {
        answerService.pinnedAnswer(answerId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{answerId}/pin")
    public ResponseEntity<Void> cancelPinnedAnswer(Long userId, @PathVariable String answerId) {
        answerService.cancelPinnedAnswer(answerId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{answerId}/like")
    public ResponseEntity<Void> registerOrCancelLike(Long userId, @PathVariable String answerId) {
        answerService.registerOrCancelLike(answerId, userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{postId}")
    public ResponseEntity<List<AnswerResponse>> retrieve(Long userId, @PathVariable String postId) {
        return ResponseEntity.ok(answerService.retrieve(postId, userId));
    }
//
//    @GetMapping
//    public ResponseEntity<List<PostResponse>> retrieve(@RequestParam(required = false, defaultValue = "0") int page,
//                                                       @RequestParam(required = false, defaultValue = "10") int size,
//                                                       @RequestParam(required = false) PostType postType,
//                                                       Long userId) {
//        return ResponseEntity.ok(postService.retrievePosts(userId, page, size, postType));
//    }
}
