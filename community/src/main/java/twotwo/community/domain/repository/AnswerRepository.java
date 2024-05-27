package twotwo.community.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import twotwo.community.domain.Answer;

import java.util.List;

public interface AnswerRepository extends MongoRepository<Answer, String> {
    List<Answer> findByPostId(String postId);
}
