package com.social.Tumblr.security.services.service;

import com.social.Tumblr.security.models.dtos.response.SearchedUsersResponseDto;
import com.social.Tumblr.security.models.dtos.request.ChangePasswordRequest;
import com.social.Tumblr.security.models.dtos.response.UserProfileResponseDto;
import com.social.Tumblr.security.models.dtos.request.UserProfileUpdateRequestDto;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface UserService {

    public String changePassword(ChangePasswordRequest request, Principal currentUser);

    public UserProfileResponseDto getCurrentUserProfile(Principal currentUser);

    public UserProfileResponseDto getUserProfile(Principal currentUser, Integer userId);

    public void updateUserProfile(UserProfileUpdateRequestDto userProfileUpdateDto, Principal currentUser, MultipartFile imageFile);

    public List<SearchedUsersResponseDto> findUsersByUserName(Principal currentUser, String userName);

    public List<SearchedUsersResponseDto> findRecentSearches(Principal currentUser);

    public Users getUserById(Integer userId);

    public String deleteUserById(Integer id);


}
