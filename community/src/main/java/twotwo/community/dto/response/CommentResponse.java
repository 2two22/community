package twotwo.community.dto.response;

import lombok.Builder;
import twotwo.community.domain.AnswerComment;
import twotwo.community.domain.Post;
import twotwo.community.domain.PostComment;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentResponse(
        String id,
        String content,
        LocalDateTime createdAt,
        UserResponse user,
        int likeCount,
        boolean isUserLiked,
        List<CommentResponse> reComments,
        int numberOfComments

) {
    public static CommentResponse from(PostComment comment, Long userId) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .user(UserResponse.from(comment.getUser()))
                .isUserLiked(comment.isUserLiked(userId))
                .likeCount(comment.getLikes().size())
                .build();
    }

    public static CommentResponse from(AnswerComment comment, Long userId) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .user(UserResponse.from(comment.getUser()))
                .isUserLiked(comment.isUserLiked(userId))
                .likeCount(comment.getLikes().size())
                .build();
    }
}
