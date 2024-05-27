package twotwo.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import twotwo.community.domain.PostType;

@Getter
public class AnswerRequest {
    @NotBlank
    private String content;
    private String postId;
}
