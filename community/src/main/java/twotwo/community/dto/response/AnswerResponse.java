package twotwo.community.dto.response;

import lombok.Builder;
import twotwo.community.domain.Answer;
import twotwo.community.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AnswerResponse(
        String id,
        String content,
        LocalDateTime createdAt,
        List<String> images,
        UserResponse user,
        int likeCount,
        boolean isUserLiked,
        boolean isPinned
) {
    public static AnswerResponse from(Answer answer, Long userId, boolean isPinned) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .images(answer.getImages())
                .createdAt(answer.getCreatedAt())
                .user(UserResponse.from(answer.getUser()))
                .isUserLiked(answer.isUserLiked(userId))
                .likeCount(answer.getLikes().size())
                .isPinned(isPinned)
                .build();
    }
}
