package twotwo.community.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import twotwo.community.client.UserClient;
import twotwo.community.domain.Post;
import twotwo.community.domain.PostComment;
import twotwo.community.domain.repository.PostCommentRepository;
import twotwo.community.domain.repository.PostRepository;
import twotwo.community.dto.response.CommentResponse;
import twotwo.community.dto.response.UserResponse;
import twotwo.community.exception.BudException;
import twotwo.community.exception.ErrorCode;
import twotwo.community.util.TokenProvider;

import java.util.Objects;

import static twotwo.community.exception.ErrorCode.NOT_FOUND_POST;

@RequiredArgsConstructor
@Service
public class PostCommentService {
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final UserClient userClient;
    private final TokenProvider tokenProvider;

    public CommentResponse create(String postId, String token, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        Long userId = tokenProvider.getId(token);
        UserResponse user = userClient.getUserInfo(token);
        post.increaseCommentCount();
        postRepository.save(post);

        return CommentResponse.from(commentRepository.save(PostComment.of(postId, content, user)), userId);
    }

    public CommentResponse createReComment(String commentId, String token, String content) {
        PostComment parent = commentRepository.findByIdAndParentCommentIdIsNull(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        Post post = postRepository.findById(parent.getPostId())
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        Long userId = tokenProvider.getId(token);
        UserResponse user = userClient.getUserInfo(token);
        post.increaseCommentCount();
        postRepository.save(post);

        return CommentResponse.from(commentRepository.save(PostComment.of(parent.getPostId(), commentId, content, user)), userId);
    }

    public CommentResponse update(String commentId, String token, String content) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));

        Long userId = tokenProvider.getId(token);
        if (!Objects.equals(userId, comment.getUser().getId()))
            throw new BudException(ErrorCode.NOT_POST_OWNER);

        UserResponse user = userClient.getUserInfo(token);
        comment.update(content, user);
        commentRepository.save(comment);

        return CommentResponse.from(comment, userId);
    }

    public String delete(String commentId, String token) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new BudException(ErrorCode.NOT_FOUND_POST));

        Long userId = tokenProvider.getId(token);
        if (!Objects.equals(userId, comment.getUser().getId()))
            throw new BudException(ErrorCode.NOT_POST_OWNER);

        post.decreaseCommentCount();
        postRepository.save(post);

        commentRepository.delete(comment);
        return commentId;
    }

    public boolean registerOrCancelLike(String commentId, String token) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        Long userId = tokenProvider.getId(token);
        comment.registerOrCancelLike(userId);
        commentRepository.save(comment);
        return true;
    }

    public Slice<CommentResponse> retrieveComments(String postId, String token, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Slice<PostComment> comments = commentRepository.findByPostIdAndParentCommentIdIsNullOrderByCreatedAtDesc(postId, request)
                .map(comment -> {
                    comment.addReComments(commentRepository.findByParentCommentId(comment.getId()));
                    return comment;
                });
        Long userId = tokenProvider.getId(token);
        return comments.map(comment -> CommentResponse.from(comment, userId));
    }
}
