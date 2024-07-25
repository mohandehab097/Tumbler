package com.social.Tumblr.posts.services.serviceimpl;

import com.social.Tumblr.posts.exceptions.ResourceNotFoundException;
import com.social.Tumblr.posts.models.dtos.LikeResponseDto;
import com.social.Tumblr.posts.models.entities.Likes;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.posts.models.repositeries.LikeRepository;
import com.social.Tumblr.posts.models.repositeries.PostRepository;
import com.social.Tumblr.posts.services.service.GoogleCloudStorageService;
import com.social.Tumblr.posts.services.service.LikeService;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private static final Logger log = LoggerFactory.getLogger(LikeServiceImpl.class);
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    public void likePost(Principal currentUser, Long postId) {
        Users user = getUserFromPrincipal(currentUser);
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Users postOwner = post.getUser();

        Optional<Likes> existLike = likeRepository.findByUserAndPost(user, post);

        if (existLike.isPresent()) {
            likeRepository.delete(existLike.get());
        } else {
            try {
                Likes like = new Likes();
                like.setUser(user);
                like.setPost(post);
                likeRepository.save(like);
            } catch (DataIntegrityViolationException e) {
                throw new IllegalStateException("User has already liked this post", e);
            }
        }

        String deviceToken = "your-ios-device-token";
        String title = "Like notification";
        String body = user.getFullName() + "liked your post" + post.getId();

//        firebaseMessagingService.sendNotificationToToken(deviceToken, title, body);
    }


    private Users getUserFromPrincipal(Principal currentUser) {
        return (Users) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();
    }

    public LikeResponseDto mapLikesToResponseDto(long id,Users user) {
        LikeResponseDto likeResponseDto = new LikeResponseDto();

        likeResponseDto.setId(id);
        likeResponseDto.setUserId(user.getId());
        likeResponseDto.setUsername(user.getFullName());

        if (user.getImage() != null) {
            String userImage = googleCloudStorageService.getFileUrl(user.getImage());
            likeResponseDto.setUserImage(userImage);
        }

        return likeResponseDto;
    }


}
