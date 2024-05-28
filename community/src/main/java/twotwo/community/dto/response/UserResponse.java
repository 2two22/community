package twotwo.community.dto.response;

import lombok.Builder;
import twotwo.community.domain.User;
import twotwo.community.dto.request.ProfileRequest;

@Builder
public record UserResponse(
        Long id,
        String nickName,
        String profileUrl
) {
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nickName(user.getNickname())
                .profileUrl(user.getProfilePath())
                .build();
    }

    public static UserResponse from(ProfileRequest request) {
        return UserResponse.builder()
                .id(request.getId())
                .nickName(request.getNickname())
                .profileUrl(request.getProfileUrl())
                .build();
    }
}
