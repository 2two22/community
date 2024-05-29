package twotwo.community.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.Lint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import twotwo.community.client.S3Client;
import twotwo.community.client.UserClient;
import twotwo.community.domain.Domain;
import twotwo.community.domain.Post;
import twotwo.community.domain.PostType;
import twotwo.community.domain.repository.PostRepository;
import twotwo.community.dto.request.PostRequest;
import twotwo.community.dto.response.PostResponse;
import twotwo.community.dto.response.UserResponse;
import twotwo.community.exception.BudException;
import twotwo.community.exception.ErrorCode;
import twotwo.community.util.TokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static twotwo.community.exception.ErrorCode.NOT_FOUND_POST;
import static twotwo.community.exception.ErrorCode.NOT_POST_OWNER;


@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final S3Client s3Client;
    private final UserClient userClient;
    private final TokenProvider tokenProvider;

    public String create(String token, List<MultipartFile> images, PostRequest request) {

        // TODO : request to user server
        UserResponse response = userClient.getUserInfo(token);
        return postRepository.save(Post.of(request, saveImages(images), response)).getTitle();
    }

    public String update(String postId, List<MultipartFile> images, PostRequest request, String token) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        Long userId = tokenProvider.getId(token);
        if (!Objects.equals(post.getUser().getId(), userId)) {
            throw new BudException(NOT_POST_OWNER);
        }
        UserResponse response = userClient.getUserInfo(token);

        post.update(request, saveImages(images), response);

        deleteImages(post);
        return request.getTitle();
    }

    public String delete(String postId, String token) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        if (!Objects.equals(post.getUser().getId(), tokenProvider.getId(token))) {
            throw new BudException(NOT_POST_OWNER);
        }
        postRepository.delete(post);
        return postId;
    }

    public boolean registerOrCancelLike(String postId, String token) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        post.registerOrCancelLike(tokenProvider.getId(token));
        postRepository.save(post);
        return true;
    }

    private List<String> saveImages(List<MultipartFile> images) {
        List<String> saved = new ArrayList<>();
        if (Objects.nonNull(images)) {
            for (MultipartFile image : images) {
                saved.add(s3Client.upload(image, Domain.POST));
            }
        }
        return saved;
    }

    private void deleteImages(Post post) {
        deleteFeedImagesFromS3(post);
    }

    private void deleteFeedImagesFromS3(Post post) {
        post.getImages().forEach(s3Client::delete);
    }


    public PostResponse retrieve(String token, String postId) {
        Long userId = tokenProvider.getId(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        return PostResponse.from(post, userId);
    }

    public Page<PostResponse> retrievePosts(String token, int page, int size, PostType type) {
        Long userId = tokenProvider.getId(token);
        PageRequest request = PageRequest.of(page, size);

        return postRepository.findAllByTypeInOrderByCreatedAtDesc(type.getMyTypes(), request)
                .map(post -> PostResponse.from(post, userId));
    }

    public Long getUsersPostCount(Long userId) {
        return postRepository.countByUser_Id(userId);
    }

    public Page<PostResponse> retrieveMyPosts(String token, Long userId, int page, int size, PostType type){
        Long tokenUserId = tokenProvider.getId(token);
        if(!Objects.equals(userId, tokenUserId))
            throw new BudException(NOT_POST_OWNER);
        PageRequest request = PageRequest.of(page, size);
        return postRepository.findAllByUser_IdAndTypeOrderByCreatedAtDesc(type, request)
                .map(post -> PostResponse.from(post, userId));
    }
}

