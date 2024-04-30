package twotwo.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twotwo.community.client.UserClient;
import twotwo.community.domain.Post;
import twotwo.community.domain.repository.PostRepository;
import twotwo.community.dto.request.PostRequest;
import twotwo.community.dto.response.PostResponse;
import twotwo.community.dto.response.UserResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserClient userClient;

    public PostResponse create(Long userId, PostRequest form) {
        // TODO : request to user server
        UserResponse response = userClient.getUserInfo(userId);
        return PostResponse.from(postRepository.save(Post.of(form, null, response)));
    }

    public PostResponse update(String postId, Long userId, PostRequest form) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));

        if (!Objects.equals(post.getUser().getId(), userId))
            throw new RuntimeException("user is not post owner");

        // TODO : request to user server
        UserResponse response = null;
        post.update(form, null, response);
        return PostResponse.from(postRepository.save(post));
    }

    public void delete(String postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));

        if (!Objects.equals(post.getUser().getId(), userId))
            throw new RuntimeException("user is not post owner");

        postRepository.delete(post);
    }

    public PostResponse retrieve(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));
        return PostResponse.from(post);
    }

    public List<PostResponse> retrievePosts() {
        return postRepository.findAll()
                .stream().map(PostResponse::from)
                .collect(Collectors.toList());
    }


}
