package com.social.Tumblr.security.controllers;

import com.social.Tumblr.security.models.dtos.request.ChangePasswordRequest;
import com.social.Tumblr.security.models.dtos.request.UserProfileUpdateRequestDto;
import com.social.Tumblr.security.services.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
@RestController
@RequestMapping("/v0/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordRequest request,
            Principal currentUser
    ) {
        String response = userService.changePassword(request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "profile/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentUserProfile(Principal currentUser) {
        return ResponseEntity.ok(userService.getCurrentUserProfile(currentUser));
    }

    @GetMapping(value = "/search/profile/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSearchUserProfile(Principal currentUser,@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.getSearchUserProfile(currentUser,userId));
    }
    @GetMapping(value = "/post/profile/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostUserProfile(Principal currentUser,@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.getPostUserProfile(currentUser,userId));
    }

    @DeleteMapping(value = "/recent-search/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRecentSearch(Principal currentUser,@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.deleteRecentSearchUser(currentUser,userId));
    }


    @PutMapping("profile")
    public ResponseEntity<?> updateUserProfile(
            @RequestPart @Valid UserProfileUpdateRequestDto userProfileUpdateDto,
            @RequestPart(value = "imageFile", required = false)  MultipartFile imageFile,
            Principal currentUser
    ) {
        userService.updateUserProfile(userProfileUpdateDto, currentUser, imageFile);
        return ResponseEntity.ok("User profile updated successfully");
    }

    @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUsersByUserName(
            Principal currentUser,
            @RequestParam String userName
    ) {
        if (userName == null || userName.trim().isEmpty()) {
            return ResponseEntity.ok(userService.findRecentSearches(currentUser));
        } else {
            return ResponseEntity.ok(userService.findUsersByUserName(currentUser, userName));
        }
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }

}
