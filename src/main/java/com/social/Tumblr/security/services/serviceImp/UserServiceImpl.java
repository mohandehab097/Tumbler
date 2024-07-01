package com.social.Tumblr.security.services.serviceImp;

import com.social.Tumblr.posts.models.enums.FollowStatus;
import com.social.Tumblr.posts.services.service.FollowerService;
import com.social.Tumblr.posts.services.service.PostService;
import com.social.Tumblr.security.models.dtos.response.UserProfileResponseDto;
import com.social.Tumblr.security.models.dtos.response.SearchedUsersResponseDto;
import com.social.Tumblr.security.models.dtos.request.ChangePasswordRequest;
import com.social.Tumblr.security.models.dtos.request.UserProfileUpdateRequestDto;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.mappers.UserMapper;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import com.social.Tumblr.security.services.service.ImagesService;
import com.social.Tumblr.security.services.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${image.path}")
    private String imagePath;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private PostService postService;


    @Override
    public String changePassword(ChangePasswordRequest request, Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        validatePasswordChange(request, user);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Your password has been changed successfully.";
    }

    @Override
    public UserProfileResponseDto getCurrentUserProfile(Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        UserProfileResponseDto userProfileDto = userMapper.mapUserToUserProfile(user);
        userProfileDto.setImage("/" + imagePath + user.getImage());
        userProfileDto.setNumberOfFollowers(getNumberOfFollowers(user));
        userProfileDto.setNumberOfFollowing(getNumberOfFollowing(user));
        userProfileDto.setNumberOfPosts(getNumberOfPosts(user));
        return userProfileDto;
    }

    @Override
    public UserProfileResponseDto getUserProfile(Principal currentUser, Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        UserProfileResponseDto userProfileDto = userMapper.mapUserToUserProfile(user);
        userProfileDto.setImage("/" + imagePath + user.getImage());
        userProfileDto.setNumberOfFollowers(getNumberOfFollowers(user));
        userProfileDto.setNumberOfFollowing(getNumberOfFollowing(user));
        userProfileDto.setNumberOfPosts(getNumberOfPosts(user));
        userProfileDto.setFollowStatus(getFollowStatus(currentUser, userId));
        return userProfileDto;
    }




    @Override
    public List<SearchedUsersResponseDto> findUsersByUserName(Principal currentUser, String userName) {
        Users user = getUserFromPrincipal(currentUser);
        List<Users> users = userRepository.findUsersByUserName(userName, user.getId());
        return users.isEmpty() ? Collections.EMPTY_LIST : userMapper.mapUsersToSearchedUsers(users);
    }

    @Override
    public void updateUserProfile(UserProfileUpdateRequestDto userProfileUpdateDto, Principal currentUser, MultipartFile imageFile) {
        Users user = getUserFromPrincipal(currentUser);

        Users userToUpdate = getUserById(user.getId());

        String newUserImage = updateImageIfNeeded(userToUpdate, imageFile);

        saveUpdatedUserDetails(userToUpdate, userProfileUpdateDto, newUserImage);
    }


    @Override
    public Users getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @Override
    public String deleteUserById(Integer id){
        if(userRepository.findById(id).isEmpty()){
            return "User id not found";
        }

          userRepository.deleteById(id);
        return "User deleted Successfully";
    }

    private boolean checkIsFollow(Principal currentUser, Integer userId) {
        return followerService.isFollowing(currentUser, userId);
    }

    private FollowStatus getFollowStatus(Principal currentUser, Integer userId) {
        return followerService.getFollowStatus(currentUser, userId);
    }

    private Long getNumberOfFollowers(Users user) {
        return followerService.getNumberFollowers(user);
    }

    private Long getNumberOfFollowing(Users user) {
        return followerService.getNumberFollowing(user);
    }

    private Long getNumberOfPosts(Users user) {
        return postService.getNumberOfPosts(user);
    }

    private String updateImageIfNeeded(Users userToUpdate, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String newImageFileName = imagesService.uploadImage(imagePath, imageFile);
            deleteOldImage(userToUpdate.getImage());
            return newImageFileName;
        }
        return userToUpdate.getImage();
    }

    private void saveUpdatedUserDetails(Users userToUpdate, UserProfileUpdateRequestDto userProfileUpdateDto, String newUserImage) {
        userToUpdate.setFullName(userProfileUpdateDto.getFullName());
        userToUpdate.setPhoneNumber(userProfileUpdateDto.getPhoneNumber());
        userToUpdate.setImage(newUserImage);
        userToUpdate.setBio(userProfileUpdateDto.getBio());

        userRepository.save(userToUpdate);
    }

    private void deleteOldImage(String oldImageFileName) {
        imagesService.deleteImage(imagePath, oldImageFileName);
    }

    private Users getUserFromPrincipal(Principal currentUser) {
        return (Users) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();
    }

    private void validatePasswordChange(ChangePasswordRequest request, Users user) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong current password.");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords do not match.");
        }
    }
}
