package com.social.Tumblr.security.services.serviceImp;

import com.social.Tumblr.posts.models.enums.FollowStatus;
import com.social.Tumblr.posts.services.service.FollowerService;
import com.social.Tumblr.posts.services.service.GoogleCloudStorageService;
import com.social.Tumblr.posts.services.service.NotificationService;
import com.social.Tumblr.posts.services.service.PostService;
import com.social.Tumblr.security.models.dtos.response.UserProfileResponseDto;
import com.social.Tumblr.security.models.dtos.response.SearchedUsersResponseDto;
import com.social.Tumblr.security.models.dtos.request.ChangePasswordRequest;
import com.social.Tumblr.security.models.dtos.request.UserProfileUpdateRequestDto;
import com.social.Tumblr.security.models.entities.RecentUserSearch;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.mappers.UserMapper;
import com.social.Tumblr.security.models.repositeries.RecentUserSearchRepository;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @Autowired
    private RecentUserSearchRepository recentUserSearchRepository;

    @Autowired
    private NotificationService notificationService;


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
        if (user.getImage() != null) {
            String imageUrl = googleCloudStorageService.getFileUrl(user.getImage());
            userProfileDto.setImage(imageUrl);
        }
        userProfileDto.setNumberOfFollowers(getNumberOfFollowers(user));
        userProfileDto.setNumberOfFollowing(getNumberOfFollowing(user));
        userProfileDto.setNumberOfPosts(getNumberOfPosts(user));
        return userProfileDto;
    }


    @Override
    public UserProfileResponseDto getSearchUserProfile(Principal currentUser, Integer userId) {
        return getUserProfile(currentUser, userId, true);
    }

    @Override
    public UserProfileResponseDto getPostUserProfile(Principal currentUser, Integer userId) {
        return getUserProfile(currentUser, userId, false);
    }

    private UserProfileResponseDto getUserProfile(Principal currentUser, Integer userId, boolean isSearch) {
        Users currentUserObject = getUserFromPrincipal(currentUser);

        Users searchedUserProfile = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        UserProfileResponseDto userProfileDto = userMapper.mapUserToUserProfile(searchedUserProfile);

        if (isSearch) {
            saveRecentSearch(currentUserObject, searchedUserProfile);
        }

        if (searchedUserProfile.getImage() != null) {
            String imageUrl = googleCloudStorageService.getFileUrl(searchedUserProfile.getImage());
            userProfileDto.setImage(imageUrl);
        }
        userProfileDto.setId(searchedUserProfile.getId());
        userProfileDto.setNumberOfFollowers(getNumberOfFollowers(searchedUserProfile));
        userProfileDto.setNumberOfFollowing(getNumberOfFollowing(searchedUserProfile));
        userProfileDto.setNumberOfPosts(getNumberOfPosts(searchedUserProfile));
        userProfileDto.setFollow(getFollowStatus(currentUser, userId));
        return userProfileDto;
    }





    @Override
    public List<SearchedUsersResponseDto> findUsersByUserName(Principal currentUser, String userName) {
        Users user = getUserFromPrincipal(currentUser);
        List<Users> users = userRepository.findUsersByUserName(userName, user.getId());
        if (users.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<SearchedUsersResponseDto> searchedUser = users.isEmpty() ? Collections.EMPTY_LIST : mapSearchUsersToSearchedUsersDto(users, currentUser);
        for (SearchedUsersResponseDto searchedUsersResponseDto : searchedUser) {
            searchedUsersResponseDto.setFollow(getFollowStatus(currentUser, searchedUsersResponseDto.getId()));
        }

        return searchedUser;
    }

    public List<SearchedUsersResponseDto> mapSearchUsersToSearchedUsersDto(List<Users> users, Principal currentUser) {
        return users.stream()
                .map(recentSearch -> mapUserSearchToSearchedUserDto(recentSearch, currentUser))
                .collect(Collectors.toList());
    }

    public SearchedUsersResponseDto mapUserSearchToSearchedUserDto(Users user, Principal currentUser) {
        SearchedUsersResponseDto dto = new SearchedUsersResponseDto();

        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        if (user.getImage() != null) {
            String imageUrl = googleCloudStorageService.getFileUrl(user.getImage());
            dto.setImage(imageUrl);
        }
        dto.setFollow(getFollowStatus(currentUser, user.getId()));

        return dto;
    }

    public List<SearchedUsersResponseDto> findRecentSearches(Principal currentUser) {
        Users currentUserObject = getUserFromPrincipal(currentUser);
        List<RecentUserSearch> recentSearches = recentUserSearchRepository.findByUserIdOrderBySearchTimeDesc(currentUserObject.getId());
        return recentSearches.isEmpty() ? Collections.EMPTY_LIST : mapRecentSearchUsersToSearchedUsersDto(recentSearches, currentUser);
    }

    public List<SearchedUsersResponseDto> mapRecentSearchUsersToSearchedUsersDto(List<RecentUserSearch> recentSearches, Principal currentUser) {
        return recentSearches.stream()
                .map(recentSearch -> mapRecentUserSearchToSearchedUserDto(recentSearch, currentUser))
                .collect(Collectors.toList());
    }

    private SearchedUsersResponseDto mapRecentUserSearchToSearchedUserDto(RecentUserSearch recentUserSearch, Principal currentUser) {
        SearchedUsersResponseDto dto = new SearchedUsersResponseDto();
        Users searchedUser = recentUserSearch.getSearchedUser();
        dto.setId(searchedUser.getId());
        dto.setFullName(searchedUser.getFullName());
        if (searchedUser.getImage() != null) {
            String imageUrl = googleCloudStorageService.getFileUrl(searchedUser.getImage());
            dto.setImage(imageUrl);
        }
        dto.setFollow(getFollowStatus(currentUser, searchedUser.getId()));

        return dto;
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
    public String deleteUserById(Integer id) {
        if (userRepository.findById(id).isEmpty()) {
            return "User id not found";
        }

        userRepository.deleteById(id);
        return "User deleted Successfully";
    }

    @Override
    public String deleteRecentSearchUser(Principal currentUser , Integer searchedUserId) {
        Users user = getUserFromPrincipal(currentUser);

        Optional<RecentUserSearch> recentUserSearch = recentUserSearchRepository.findByUserIdAndSearchedUserId(user.getId(),searchedUserId);

        if (!recentUserSearch.isPresent()) {
            return "Recent searched User not found";
        }


        recentUserSearchRepository.deleteById(recentUserSearch.get().getId());
        return "Recent Searched User deleted Successfully";
    }

    private boolean checkIsFollow(Principal currentUser, Integer userId) {
        return followerService.isFollowing(currentUser, userId);
    }

    private boolean getFollowStatus(Principal currentUser, Integer userId) {
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
            return imagesService.uploadImage(imageFile);
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

    public void saveRecentSearch(Users user, Users searchedUser) {
        Optional<RecentUserSearch> existingSearch = recentUserSearchRepository.findByUserIdAndSearchedUserId(user.getId(), searchedUser.getId());
        RecentUserSearch recentSearch;

        if (existingSearch.isPresent()) {
            recentSearch = existingSearch.get();
            recentSearch.setSearchTime(LocalDateTime.now());
        } else {
            recentSearch = new RecentUserSearch();
            recentSearch.setUser(user);
            recentSearch.setSearchedUser(searchedUser);
            recentSearch.setSearchTime(LocalDateTime.now());
        }

        recentUserSearchRepository.save(recentSearch);
    }

    public UserProfileResponseDto getUserProfileFromNotification(Principal currentUser, Integer userId,Long notificationId){
        UserProfileResponseDto userProfileResponseDto = getUserProfile(currentUser,userId,false);
        notificationService.markNotificationAsRead(notificationId);
        return userProfileResponseDto;
    }

}
