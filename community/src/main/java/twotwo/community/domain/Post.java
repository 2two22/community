package twotwo.community.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import twotwo.community.dto.request.PostRequest;
import twotwo.community.dto.response.UserResponse;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "posts")
public class Post extends BaseTimeDocument {
    @Id
    private String id;
    private String title;
    private String content;
    private List<String> images;
    private User user;

    public static Post of(PostRequest form, List<String> images, UserResponse user) {
        return Post.builder()
                .title(form.title())
                .content(form.content())
                .images(images)
                .user(User.from(user))
                .build();
    }

    public void update(PostRequest form, List<String> images, UserResponse user) {
        this.title = form.title();
        this.content = form.content();
        this.images = images;
        this.user = User.from(user);
    }
}
