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
public class PostRequest{
    @NotNull
    private PostType postType;

    @NotBlank
    private String title;

    private String content;
}
