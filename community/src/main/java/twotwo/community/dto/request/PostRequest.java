package twotwo.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import twotwo.community.domain.PostType;

@Getter
public class PostRequest{
    @NotNull
    private PostType postType;

    @NotBlank
    private String title;

    private String content;
}
