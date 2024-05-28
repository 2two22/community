package twotwo.community.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import twotwo.community.dto.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "post-comments")
public class PostComment extends BaseTimeDocument{
    @Id
    private String id;
    private String postId;
    private String parentCommentId;
    private String content;
    private User user;
    private boolean isDeleted;
    @Builder.Default
    private List<Long> likes = new ArrayList<>();
    @Transient
    private List<PostComment> reComments;

    public static PostComment of(String postId, String content, UserResponse user){
        return PostComment.builder()
                .postId(postId)
                .content(content)
                .user(User.from(user))
                .build();
    }

    public static PostComment of(String postId, String parentCommentId, String content, UserResponse user){
        return PostComment.builder()
                .postId(postId)
                .content(content)
                .parentCommentId(parentCommentId)
                .user(User.from(user))
                .build();
    }

    public void update(UserResponse user){
        this.user = User.from(user);
    }

    public void update(String content, UserResponse user){
        this.content = content;
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

    public void addReComments(List<PostComment> reComments){
        this.reComments = reComments;
    }
}
