package twotwo.community.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import twotwo.community.client.UserClient;
import twotwo.community.domain.Answer;
import twotwo.community.domain.AnswerComment;
import twotwo.community.domain.repository.AnswerCommentRepository;
import twotwo.community.domain.repository.AnswerRepository;
import twotwo.community.dto.response.CommentResponse;
import twotwo.community.dto.response.UserResponse;
import twotwo.community.exception.BudException;
import twotwo.community.exception.ErrorCode;
import twotwo.community.util.TokenProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static twotwo.community.exception.ErrorCode.NOT_FOUND_POST;

@RequiredArgsConstructor
@Service
public class AnswerCommentService {
    private final AnswerRepository answerRepository;
    private final AnswerCommentRepository commentRepository;
    private final UserClient userClient;
    private final TokenProvider tokenProvider;

    public CommentResponse create(String answerId, String token, String content) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        UserResponse user = userClient.getUserInfo(token);
        answer.increaseCommentCount();
        answerRepository.save(answer);

        return CommentResponse.from(commentRepository.save(AnswerComment.of(answerId, content, user)), user.id());
    }

    public CommentResponse createReComment(String commentId, String token, String content) {
        AnswerComment parent = commentRepository.findByIdAndParentCommentIdIsNull(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        Answer answer = answerRepository.findById(parent.getAnswerId())
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        UserResponse user = userClient.getUserInfo(token);
        answer.increaseCommentCount();
        answerRepository.save(answer);

        return CommentResponse.ofRecomment(commentRepository.save(AnswerComment.of(parent.getAnswerId(), commentId, content, user)), user.id());
    }

    public CommentResponse update(String commentId, String token, String content) {
        AnswerComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        if (!Objects.equals(tokenProvider.getId(token), comment.getUser().getId()))
            throw new BudException(ErrorCode.NOT_POST_OWNER);
        UserResponse user = userClient.getUserInfo(token);
        comment.update(content, user);
        commentRepository.save(comment);

        return CommentResponse.from(comment, user.id());
    }

    public String delete(String commentId, String token) {
        AnswerComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        Answer answer = answerRepository.findById(comment.getAnswerId())
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));

        if (!Objects.equals(tokenProvider.getId(token), comment.getUser().getId()))
            throw new BudException(ErrorCode.NOT_POST_OWNER);

        answer.decreaseCommentCount();
        answerRepository.save(answer);

        commentRepository.delete(comment);
        return commentId;
    }

    public boolean registerOrCancelLike(String commentId, String token) {
        AnswerComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        comment.registerOrCancelLike(tokenProvider.getId(token));
        commentRepository.save(comment);
        return true;
    }

    public List<CommentResponse> retrieveComments(String postId, String token) {
        Long userId = tokenProvider.getId(token);
        List<AnswerComment> comments = commentRepository.findByAnswerIdAndParentCommentIdIsNullOrderByCreatedAtDesc(postId).stream()
                .peek(comment -> comment.addReComments(commentRepository.findByParentCommentId(comment.getId()))).collect(Collectors.toList());
        return comments.stream().map(comment -> CommentResponse.from(comment, userId)).collect(Collectors.toList());
    }
}
