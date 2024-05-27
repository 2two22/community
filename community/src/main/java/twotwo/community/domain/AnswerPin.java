package twotwo.community.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "answer-pins")
public class AnswerPin extends BaseTimeDocument{
    @Id
    private String postId;
    private String answerId;

    public static AnswerPin of(String postId, String answerId){
        return AnswerPin.builder()
                .postId(postId)
                .answerId(answerId)
                .build();
    }

    public void update(String answerId){
        this.answerId = answerId;
    }
}
