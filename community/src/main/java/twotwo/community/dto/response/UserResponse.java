package twotwo.community.dto.response;

import lombok.Builder;
import twotwo.community.domain.User;

@Builder
public record UserResponse(
        Long id,
        String nickname,
        String profilePath
) {
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .build();
    }
}