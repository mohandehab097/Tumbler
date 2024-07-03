package com.social.Tumblr.posts.services.serviceimpl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.social.Tumblr.posts.exceptions.ResourceNotFoundException;
import com.social.Tumblr.posts.models.entities.Likes;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.posts.models.repositeries.LikeRepository;
import com.social.Tumblr.posts.models.repositeries.PostRepository;
import com.social.Tumblr.posts.services.service.LikeService;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void likePost(Principal currentUser, Long postId) {
        Users user = getUserFromPrincipal(currentUser);
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Users postOwner = post.getUser();

        Optional<Likes> existLike = likeRepository.findByUserAndPost(user, post);

        if (existLike.isPresent()) {
            likeRepository.delete(existLike.get());
        }
        else{
            Likes like = new Likes();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
        }

        String deviceToken = "your-ios-device-token";
        String title = "Like notification";
        String body = user.getFullName()+"liked your post"+post.getId();

//        firebaseMessagingService.sendNotificationToToken(deviceToken, title, body);
    }


    private Users getUserFromPrincipal(Principal currentUser) {
        return (Users) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();
    }
}
