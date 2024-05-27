package twotwo.community.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twotwo.community.client.S3Client;
import twotwo.community.client.UserClient;
import twotwo.community.domain.Answer;
import twotwo.community.domain.AnswerPin;
import twotwo.community.domain.Domain;
import twotwo.community.domain.Post;
import twotwo.community.domain.repository.AnswerPinRepository;
import twotwo.community.domain.repository.AnswerRepository;
import twotwo.community.domain.repository.PostRepository;
import twotwo.community.dto.request.AnswerRequest;
import twotwo.community.dto.response.AnswerResponse;
import twotwo.community.dto.response.UserResponse;
import twotwo.community.exception.BudException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static twotwo.community.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;
    private final UserClient userClient;
    private final S3Client s3Client;
    private final AnswerPinRepository answerPinRepository;

    public String create(Long userId, List<MultipartFile> images, AnswerRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        if (Objects.equals(post.getUser().getId(), userId)) {
            throw new BudException(CANNOT_ANSWER_YOURSELF);
        }
        // TODO : request to user server
        UserResponse response = userClient.getUserInfo(userId);
        return answerRepository.save(Answer.of(request, saveImages(images), response)).getContent();
    }

    public String update(String answerId, List<MultipartFile> images, AnswerRequest request, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        UserResponse response = userClient.getUserInfo(userId);
        if (!Objects.equals(answer.getUser().getId(), userId)) {
            throw new BudException(NOT_POST_OWNER);
        }

        if (answerPinRepository.existsByAnswerIdAndPostId(answerId, answer.getPostId()))
            throw new BudException(CHANGE_IMPOSSIBLE_PINNED_ANSWER);

        answer.update(request, saveImages(images), response);

        deleteImages(answer);
        return answerId;
    }

    public String pinnedAnswer(String answerId, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        Post post = postRepository.findById(answer.getPostId())
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        if (!Objects.equals(post.getUser().getId(), userId)) {
            throw new BudException(NOT_POST_OWNER);
        }
        AnswerPin answerPin = answerPinRepository.findById(answer.getPostId())
                .orElse(AnswerPin.of(post.getId(), answerId));
        answerPin.update(post.getId(), answerId);
        return answerPinRepository.save(answerPin).getPostId();
    }

    public String cancelPinnedAnswer(String answerId, Long userId) {
        AnswerPin answerPin = answerPinRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new BudException(NOT_FOUND_QNA_ANSWER_PIN));
        Post post = postRepository.findById(answerPin.getPostId())
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        if (!Objects.equals(post.getUser().getId(), userId))
            throw new BudException(NOT_POST_OWNER);
        answerPinRepository.delete(answerPin);
        return answerId;
    }

    private List<String> saveImages(List<MultipartFile> images) {
        List<String> saved = new ArrayList<>();
        if (Objects.nonNull(images)) {
            for (MultipartFile image : images) {
                saved.add(s3Client.upload(image, Domain.POST));
            }
        }
        return saved;
    }

    private void deleteImages(Answer answer) {
        answer.getImages().forEach(s3Client::delete);
    }

    public String delete(String answerId, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        if (!Objects.equals(answer.getUser().getId(), userId)) {
            throw new BudException(NOT_POST_OWNER);
        }
        answerPinRepository.findByAnswerId(answerId)
                .ifPresent(answerPinRepository::delete);
        answerRepository.delete(answer);
        return answerId;
    }

    public boolean registerOrCancelLike(String answerId, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        answer.registerOrCancelLike(userId);
        answerRepository.save(answer);
        return true;
    }

    public List<AnswerResponse> retrieve(String postId, Long userId){
        List<Answer> answers = answerRepository.findByPostId(postId);
        AnswerPin answerPin = answerPinRepository.findById(postId)
                .orElseThrow(() -> new BudException(NOT_FOUND_QNA_ANSWER_PIN));
        return answers.stream().map(answer -> AnswerResponse.from(answer, userId, answer.getId().equals(answerPin.getAnswerId())))
                .sorted((o1, o2) -> Boolean.compare(o2.isPinned(), o1.isPinned())).collect(Collectors.toList());
    }

}
