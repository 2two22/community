package twotwo.community.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

//        if (Objects.nonNull(post.getQnaAnswerPin())) {
//            throw new BudException(CHANGE_IMPOSSIBLE_PINNED_ANSWER);
//        }

        post.update(request, saveImages(images), response);

        deleteImages(post);
        return request.getTitle();
    }

//    public Page<SearchPost.Response> searchPosts(Member member,
//                                                 String keyword,
//                                                 PostSortType sort,
//                                                 Order order,
//                                                 int page,
//                                                 int size,
//                                                 PostType postType) {
//
//        Page<PostDto> posts = postQuerydsl.findAllByPostStatus(member.getId(),
//                keyword, sort, order, PageRequest.of(page, size), postType);
//
//        return new PageImpl<>(
//                posts.stream()
//                        .map(post -> SearchPost.Response.of(post,
//                                postImageQuerydsl
//                                        .findImagePathAllByPostId(post.getId())))
//                        .collect(Collectors.toList()),
//                posts.getPageable(),
//                posts.getTotalElements());
//    }

//    public Page<SearchMyPagePost.Response> searchMyPagePosts(Member member,
//                                                             Long myPageUserId,
//                                                             PostType postType,
//                                                             Pageable pageable) {
//
//        Page<PostDto> posts = postQuerydsl.findAllByMyPagePost(
//                member.getId(),
//                myPageUserId,
//                postType,
//                pageable
//        );
//
//        return new PageImpl<>(
//                posts.stream()
//                        .map(post -> SearchMyPagePost.Response.of(post,
//                                postImageQuerydsl.findImagePathAllByPostId(post.getId())))
//                        .collect(Collectors.toList()),
//                pageable,
//                posts.getTotalElements()
//        );
//    }

//    public SearchPost.Response searchPost(Member member, Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
//
//        PostDto postDto =
//                postQuerydsl.findByPostId(member.getId(), postId);
//
//        post.hitCountUp();
//
//        postRepository.save(post);
//
//        return SearchPost.Response.of(postDto,
//                postImageQuerydsl.findImagePathAllByPostId(postId));
//    }

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

//    private void deleteQnaAnswerImagesFromS3(QnaAnswer qnaAnswer) {
//        qnaAnswer.getQnaAnswerImages()
//                .forEach(image -> s3Client.deleteImage(image.getImagePath()));
//    }


    public PostResponse retrieve(String token, String postId) {
        Long userId = tokenProvider.getId(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BudException(NOT_FOUND_POST));
        return PostResponse.from(post, userId);
    }

    public List<PostResponse> retrievePosts(String token, int page, int size, PostType type) {
        Long userId = tokenProvider.getId(token);
        PageRequest request = PageRequest.of(page, size);
        return postRepository.findAllByTypeOrderByCreatedAtDesc(type, request)
                .stream().map(post -> PostResponse.from(post, userId))
                .collect(Collectors.toList());
    }

    public Long getUsersPostCount(Long userId){
        return postRepository.countByUser_Id(userId);
    }
}

