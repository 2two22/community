package twotwo.community.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import twotwo.community.domain.AnswerComment;

import java.util.List;
import java.util.Optional;

public interface AnswerCommentRepository extends MongoRepository<AnswerComment, String> {
    List<AnswerComment> findByAnswerIdAndParentCommentIdIsNullOrderByCreatedAtDesc(String answerId);

    Optional<AnswerComment> findByIdAndParentCommentIdIsNull(String commentId);

    List<AnswerComment> findByParentCommentId(String parentCommentId);
    List<AnswerComment> findByUser_Id(Long userId);
}
