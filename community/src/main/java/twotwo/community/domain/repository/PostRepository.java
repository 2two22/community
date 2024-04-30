package twotwo.community.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import twotwo.community.domain.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

}
