package twotwo.community.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import twotwo.community.domain.Answer;
import twotwo.community.domain.AnswerPin;

import java.util.Optional;

public interface AnswerPinRepository extends MongoRepository<AnswerPin, String> {
    boolean existsByAnswerIdAndPostId(String answerId, String postId);
    Optional<AnswerPin> findByAnswerId(String answerId);
}
