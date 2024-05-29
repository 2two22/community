package twotwo.community.dto.response;

import lombok.Builder;
import twotwo.community.domain.Post;
import twotwo.community.domain.PostType;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponse(
        PostType type,
        String id,
        String title,
        String content,
        LocalDateTime createdAt,
        List<String> imageUrls,
        UserResponse member,
        int likeCount,
        int commentCount,
        boolean like
) {
    public static PostResponse from(Post post, Long userId) {
        return PostResponse.builder()
                .type(post.getType())
                .id(post.getId())
                .content(post.getContent())
                .title(post.getTitle())
                .imageUrls(post.getImages())
                .createdAt(post.getCreatedAt())
                .member(UserResponse.from(post.getUser()))
                .commentCount(post.getCommentCount())
                .like(post.isUserLiked(userId))
                .likeCount(post.getLikes().size())
                .build();
    }
}
