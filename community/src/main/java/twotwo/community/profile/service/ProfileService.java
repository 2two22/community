package twotwo.community.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twotwo.community.domain.repository.AnswerCommentRepository;
import twotwo.community.domain.repository.AnswerRepository;
import twotwo.community.domain.repository.PostCommentRepository;
import twotwo.community.domain.repository.PostRepository;
import twotwo.community.dto.request.ProfileRequest;
import twotwo.community.dto.response.UserResponse;
import twotwo.community.util.TokenProvider;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final AnswerCommentRepository answerCommentRepository;
    private final TokenProvider tokenProvider;

    public void updateWriterProfile(String token, ProfileRequest form) {
        Long userId = tokenProvider.getId(token);
        UserResponse user = UserResponse.from(form);
        answerRepository.saveAll(answerRepository.findByUser_Id(userId)
                .stream().peek(answer -> answer.update(user)).collect(Collectors.toList()));
        postRepository.saveAll(postRepository.findByUser_Id(userId)
                .stream().peek(post -> post.update(user)).collect(Collectors.toList()));
        postCommentRepository.saveAll(postCommentRepository.findByUser_Id(userId)
                .stream().peek(postComment -> postComment.update(user)).collect(Collectors.toList()));
        answerCommentRepository.saveAll(answerCommentRepository.findByUser_Id(userId)
                .stream().peek(answerComment -> answerComment.update(user)).collect(Collectors.toList()));
    }
}
