package twotwo.community.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import twotwo.community.dto.request.AnswerRequest;
import twotwo.community.dto.request.PostRequest;
import twotwo.community.dto.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "answers")
public class Answer extends BaseTimeDocument {
    @Id
    private String id;
    private String postId;
    private String content;
    @Builder.Default
    private List<String> images = new ArrayList<>();
    @Builder.Default
    private List<Long> likes = new ArrayList<>();
    private User user;
    private int commentCount;


    public static Answer of(AnswerRequest form, List<String> images, UserResponse user) {
        return Answer.builder()
                .postId(form.getPostId())
                .content(form.getContent())
                .images(images)
                .user(User.from(user))
                .build();
    }

    public void update(UserResponse user){
        this.user = User.from(user);
    }

    public void update(AnswerRequest form, List<String> images, UserResponse user) {
        this.content = form.getContent();
        this.images = images;
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
