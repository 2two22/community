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

import java.util.Objects;

import static twotwo.community.exception.ErrorCode.NOT_FOUND_POST;

@RequiredArgsConstructor
@Service
public class AnswerCommentService {
    private final AnswerRepository answerRepository;
    private final AnswerCommentRepository commentRepository;
    private final UserClient userClient;

    public CommentResponse create(String answerId, Long userId, String content) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        UserResponse user = userClient.getUserInfo(userId);
        answer.increaseCommentCount();
        answerRepository.save(answer);

        return CommentResponse.from(commentRepository.save(AnswerComment.of(answerId, content, user)), userId);
    }

    public CommentResponse createReComment(String commentId, Long userId, String content) {
        AnswerComment parent = commentRepository.findByIdAndParentCommentIdIsNull(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        Answer answer = answerRepository.findById(parent.getAnswerId())
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        UserResponse user = userClient.getUserInfo(userId);
        answer.increaseCommentCount();
        answerRepository.save(answer);

        return CommentResponse.from(commentRepository.save(AnswerComment.of(parent.getAnswerId(), commentId, content, user)), userId);
    }

    public CommentResponse update(String commentId, Long userId, String content) {
        AnswerComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));

        if (!Objects.equals(userId, comment.getUser().getId()))
            throw new BudException(ErrorCode.NOT_POST_OWNER);

        UserResponse user = userClient.getUserInfo(userId);
        comment.update(content, user);
        commentRepository.save(comment);

        return CommentResponse.from(comment, userId);
    }

    public String delete(String commentId, Long userId) {
        AnswerComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        Answer answer = answerRepository.findById(comment.getAnswerId())
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));

        if (!Objects.equals(userId, comment.getUser().getId()))
            throw new BudException(ErrorCode.NOT_POST_OWNER);

        answer.decreaseCommentCount();
        answerRepository.save(answer);

        commentRepository.delete(comment);
        return commentId;
    }

    public boolean registerOrCancelLike(String commentId, Long userId) {
        AnswerComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        comment.registerOrCancelLike(userId);
        commentRepository.save(comment);
        return true;
    }

    public Slice<CommentResponse> retrieveComments(String postId, Long userId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Slice<AnswerComment> comments = commentRepository.findByAnswerIdAndParentCommentIdIsNullOrderByCreatedAtDesc(postId, request)
                .map(comment -> {
                    comment.addReComments(commentRepository.findByParentCommentId(comment.getId()));
                    return comment;
                });
        return comments.map(comment -> CommentResponse.from(comment, userId));
    }
}
