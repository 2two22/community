package twotwo.community.dto.response;

import lombok.Builder;
import twotwo.community.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponse(
        String id,
        String title,
        String content,
        LocalDateTime createdAt,
        List<String> images,
        UserResponse user,
        int likeCount,
        boolean isUserLiked
) {
    public static PostResponse from(Post post, Long userId) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .title(post.getTitle())
                .images(post.getImages())
                .createdAt(post.getCreatedAt())
                .user(UserResponse.from(post.getUser()))
                .isUserLiked(post.isUserLiked(userId))
                .likeCount(post.getLikes().size())
                .build();
    }
}
