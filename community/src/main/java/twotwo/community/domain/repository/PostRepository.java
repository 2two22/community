package twotwo.community.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import twotwo.community.domain.Post;
import twotwo.community.domain.PostType;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Long countByUser_Id(Long userId);
    //Optional<Post> findById(String id);
    Page<Post> findAllByTypeInOrderByCreatedAtDesc(List<PostType> types, PageRequest request);
    Page<Post> findAllByUser_IdAndTypeOrderByCreatedAtDesc(Long userId, PostType type, PageRequest request);
    boolean findByIdAndType(String id, PostType type);
    List<Post> findByUser_Id(Long userId);
}
