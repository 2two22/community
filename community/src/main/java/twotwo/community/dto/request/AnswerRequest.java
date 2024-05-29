package twotwo.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import twotwo.community.domain.PostType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {
    @NotBlank
    private String content;
    private String postId;
}
