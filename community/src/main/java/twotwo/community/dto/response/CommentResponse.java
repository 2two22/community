package twotwo.community.dto.response;

import lombok.Builder;
import twotwo.community.domain.AnswerComment;
import twotwo.community.domain.Post;
import twotwo.community.domain.PostComment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                .reComments(new ArrayList<>(comment.getReComments().stream().map(re->ofRecomment(re, userId)).collect(Collectors.toList())))
                .build();
    }

    public static CommentResponse ofRecomment(PostComment comment, Long userId) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .user(UserResponse.from(comment.getUser()))
                .isUserLiked(comment.isUserLiked(userId))
                .likeCount(comment.getLikes().size())
                .build();
    }

    public static CommentResponse ofRecomment(AnswerComment comment, Long userId) {
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
                .reComments(new ArrayList<>(comment.getReComments().stream().map(re->ofRecomment(re, userId)).collect(Collectors.toList())))
                .likeCount(comment.getLikes().size())
                .build();
    }
}
