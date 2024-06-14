package twotwo.community.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<String> create(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @RequestPart AnswerRequest createQnaAnswerRequest, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(answerService.create(token, images, createQnaAnswerRequest));
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<String> update(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String answerId, @RequestPart AnswerRequest updateQnaAnswerRequest, @RequestPart(required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(answerService.update(answerId, images, updateQnaAnswerRequest, token));
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> delete(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String answerId) {
        answerService.delete(answerId, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{answerId}/pin")
    public ResponseEntity<Void> pinnedAnswer(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String answerId) {
        answerService.pinnedAnswer(answerId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{answerId}/pin")
    public ResponseEntity<Void> cancelPinnedAnswer(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String answerId) {
        answerService.cancelPinnedAnswer(answerId, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{answerId}/like")
    public ResponseEntity<Void> registerOrCancelLike(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String answerId) {
        answerService.registerOrCancelLike(answerId, token);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{postId}")
    public ResponseEntity<List<AnswerResponse>> retrieve(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String postId) {
        return ResponseEntity.ok(answerService.retrieve(postId, token));
    }
}
