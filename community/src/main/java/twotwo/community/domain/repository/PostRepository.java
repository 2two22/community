package twotwo.community.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import twotwo.community.domain.Post;
import twotwo.community.domain.PostType;

import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Long countByUser_Id(Long userId);
    Optional<Post> findById(String id);
    Page<Post> findAllByTypeOrderByCreatedAtDesc(PostType type, PageRequest request);
}
