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
@Document(collection = "answer-comments")
public class AnswerComment extends BaseTimeDocument {
    @Id
    private String id;
    private String answerId;
    private String parentCommentId;
    private String content;
    private User user;
    private boolean isDeleted;
    @Builder.Default
    private List<Long> likes = new ArrayList<>();
    @Transient
    private List<AnswerComment> reComments;

    public static AnswerComment of(String answerId, String content, UserResponse user) {
        return AnswerComment.builder()
                .answerId(answerId)
                .content(content)
                .user(User.from(user))
                .build();
    }

    public static AnswerComment of(String answerId, String parentCommentId, String content, UserResponse user) {
        return AnswerComment.builder()
                .answerId(answerId)
                .content(content)
                .parentCommentId(parentCommentId)
                .user(User.from(user))
                .build();
    }

    public void update(String content, UserResponse user) {
        this.content = content;
        this.user = User.from(user);
    }

    public void update(UserResponse user){
        this.user = User.from(user);
    }

    public void registerOrCancelLike(Long userId) {
        if (isUserLiked(userId))
            cancelLike(userId);
        else addLike(userId);
    }

    private void addLike(Long userId) {
        likes.add(userId);
    }

    private void cancelLike(Long userId) {
        likes.remove(userId);
    }

    public boolean isUserLiked(Long userId) {
        return likes.contains(userId);
    }


    public void addReComments(List<AnswerComment> reComments) {
        this.reComments = reComments;
    }
}
