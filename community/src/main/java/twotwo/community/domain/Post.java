package twotwo.community.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import twotwo.community.dto.request.PostRequest;
import twotwo.community.dto.response.UserResponse;

import java.util.ArrayList;
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
    @Builder.Default
    private List<String> images = new ArrayList<>();
    @Builder.Default
    private List<Long> likes = new ArrayList<>();
    private int commentCount;
    private User user;
    private PostType type;


    public static Post of(PostRequest form, List<String> images, UserResponse user) {
        return Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .images(images)
                .user(User.from(user))
                .build();
    }

    public void update(PostRequest form, List<String> images, UserResponse user) {
        this.title = form.getTitle();
        this.content = form.getContent();
        this.images = images;
        this.user = User.from(user);
    }

    public void update(UserResponse user){
        this.user = User.from(user);
    }

    public void registerOrCancelLike(Long userId){
        if(isUserLiked(userId))
            cancelLike(userId);
        else addLike(userId);
    }

    private void addLike(Long userId){
        likes.add(userId);
    }

    private void cancelLike(Long userId){
        likes.remove(userId);
    }

    public boolean isUserLiked(Long userId){
        return likes.contains(userId);
    }

    public void increaseCommentCount(){
        commentCount++;
    }

    public void decreaseCommentCount(){
        commentCount--;
    }
}
