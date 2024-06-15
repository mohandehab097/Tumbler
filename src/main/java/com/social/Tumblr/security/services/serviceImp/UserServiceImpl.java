package com.social.Tumblr.security.services.serviceImp;

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


    @Override
    public String changePassword(ChangePasswordRequest request, Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        validatePasswordChange(request, user);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Your password has been changed successfully.";
    }

    @Override
    public UserProfileResponseDto getUserProfile(Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        UserProfileResponseDto userProfileDto = userMapper.mapUserToUserProfile(user);
        userProfileDto.setImage("/" + imagePath + user.getImage());
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

    private Users getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
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

}
