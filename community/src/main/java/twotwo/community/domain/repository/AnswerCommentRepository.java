package twotwo.community.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import twotwo.community.domain.AnswerComment;

import java.util.List;
import java.util.Optional;

public interface AnswerCommentRepository extends MongoRepository<AnswerComment, String> {
    Page<AnswerComment> findByAnswerIdAndParentCommentIdIsNullOrderByCreatedAtDesc(String answerId, PageRequest request);

    Optional<AnswerComment> findByIdAndParentCommentIdIsNull(String commentId);

    List<AnswerComment> findByParentCommentId(String parentCommentId);
}
