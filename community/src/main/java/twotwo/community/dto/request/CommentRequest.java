package twotwo.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequest {
    @NotBlank
    private String content;
}
