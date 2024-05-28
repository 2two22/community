package twotwo.community.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import twotwo.community.domain.AnswerPin;
import twotwo.community.domain.PostComment;

import java.util.List;
import java.util.Optional;

public interface PostCommentRepository extends MongoRepository<PostComment, String> {
    Page<PostComment> findByPostIdAndParentCommentIdIsNullOrderByCreatedAtDesc(String postId, PageRequest request);
    Optional<PostComment> findByIdAndParentCommentIdIsNull(String commentId);
    List<PostComment> findByParentCommentId(String parentCommentId);
    List<PostComment> findByUser_Id(Long userId);
}
