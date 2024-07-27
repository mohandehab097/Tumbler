package com.social.Tumblr.posts.services.serviceimpl;

import com.social.Tumblr.posts.models.dtos.StoryDto;
import com.social.Tumblr.posts.models.dtos.StoryViewDto;
import com.social.Tumblr.posts.models.dtos.ViewsDto;
import com.social.Tumblr.posts.models.entities.Story;
import com.social.Tumblr.posts.models.entities.StoryView;
import com.social.Tumblr.posts.models.repositeries.StoryRepository;
import com.social.Tumblr.posts.models.repositeries.StoryViewRepository;
import com.social.Tumblr.posts.services.service.FollowerService;
import com.social.Tumblr.posts.services.service.GoogleCloudStorageService;
import com.social.Tumblr.posts.services.service.StoryService;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import com.social.Tumblr.security.services.service.ImagesService;
import com.social.Tumblr.security.utils.TimeUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @Autowired
    private StoryViewRepository storyViewRepository;

    @Transactional
    @Override
    public String createStory(Principal currentUser, MultipartFile image) {
        Users user = getUserFromPrincipal(currentUser);

        Story story = new Story();
        story.setUser(user);
        String imageFileName = imagesService.uploadImage(image);

        story.setImage(imageFileName);
        story.setExpiresAt(LocalDateTime.now().plusHours(24));

        try {
            storyRepository.save(story);
            return "Story created successfully";
        } catch (Exception e) {
            return "failure in creation of story";
        }

    }

    @Override
    public List<StoryDto> getStoriesFromFollowedUsers(Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);

        List<Integer> followingUsersIds = followerService.findAllFollowedUserByCurrentUser(user.getId());
        List<Story> stories = storyRepository.findAllByUserIdsAndExpiresAtAfter(followingUsersIds, LocalDateTime.now());

        List<StoryDto> storyDtos = stories.stream()
                .map(story -> mapStoryToStoryDto(story, user))
                .sorted(Comparator.comparing(StoryDto::isWatched))
                .collect(Collectors.toList());

        return storyDtos;
    }

    @Override
    public StoryViewDto getStoryOfUser(Long storyId, Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        Story story = storyRepository.findByIdAndExpiresAtAfter(storyId, LocalDateTime.now());

        if (story == null) {
            throw new EntityNotFoundException("Story not found or has expired");
        }

        Optional<StoryView> existingView = storyViewRepository.findByStoryAndUser(story, user);
        if (existingView.isEmpty()) {
            StoryView storyView = new StoryView();
            storyView.setStory(story);
            storyView.setUser(user);
            storyView.setViewedAt(LocalDateTime.now());
            storyViewRepository.save(storyView);
        }

        StoryViewDto storyViewDto = mapStoryToStoryViewDto(story);

        return storyViewDto;
    }

    public List<ViewsDto> getStoryViewsDetails(Long storyId, Principal currentUser) {
        List<StoryView> views = storyViewRepository.findAllByStoryId(storyId);
        List<ViewsDto> viewsDtos = views.stream().map(storyView -> mapToStoryView(storyView, currentUser)).collect(Collectors.toList());
        return viewsDtos;
    }

    private ViewsDto mapToStoryView(StoryView storyView, Principal currentUser) {
        ViewsDto viewsDto = new ViewsDto();
        viewsDto.setUserId(storyView.getUser().getId());
        viewsDto.setUsername(storyView.getUser().getFullName());
        viewsDto.setTimeAgo(TimeUtil.calculateTimeAgo(storyView.getViewedAt()));
        if (storyView.getUser().getImage() != null) {
            String image = googleCloudStorageService.getFileUrl(storyView.getUser().getImage());
            viewsDto.setUserImage(image);
        }
        Integer numberOfViews = storyViewRepository.countByStory(storyView.getStory());
        viewsDto.setNumberOfViews(numberOfViews);
        viewsDto.setFollow(followerService.getFollowStatus(currentUser, storyView.getUser().getId()));
        return viewsDto;
    }

    private StoryViewDto mapStoryToStoryViewDto(Story story) {
        StoryViewDto storyViewDto = new StoryViewDto();

        if (story.getImage() != null) {
            String storyImage = googleCloudStorageService.getFileUrl(story.getImage());
            storyViewDto.setStoryImage(storyImage);
        }

        storyViewDto.setUserId(story.getUser().getId());
        storyViewDto.setUsername(story.getUser().getFullName());
        storyViewDto.setTimeAgo(TimeUtil.calculateTimeAgo(story.getCreatedAt()));
        if (story.getUser().getImage() != null) {
            String image = googleCloudStorageService.getFileUrl(story.getUser().getImage());
            storyViewDto.setUserImage(image);
        }

        Integer numberOfViews = storyViewRepository.countByStory(story);
        storyViewDto.setNumberOfViews(numberOfViews);

        return storyViewDto;
    }

    private StoryDto mapStoryToStoryDto(Story story, Users currentUser) {
        StoryDto storyDto = new StoryDto();
        storyDto.setStoryId(story.getId());
        if (story.getImage() != null) {
            String storyImage = googleCloudStorageService.getFileUrl(story.getImage());
            storyDto.setStoryImage(storyImage);
        }

        storyDto.setUserId(story.getUser().getId());
        storyDto.setUsername(story.getUser().getFullName());
        storyDto.setTimeAgo(TimeUtil.calculateTimeAgo(story.getCreatedAt()));
        if (story.getUser().getImage() != null) {
            String image = googleCloudStorageService.getFileUrl(story.getUser().getImage());
            storyDto.setUserImage(image);
        }
        Optional<StoryView> existingView = storyViewRepository.findByStoryAndUser(story, currentUser);


        storyDto.setWatched(existingView.isPresent());

        return storyDto;
    }

    private void saveStoryView(Story story, Users user) {
        StoryView storyView = new StoryView();
        storyView.setStory(story);
        storyView.setUser(user);
        storyView.setViewedAt(LocalDateTime.now());
        storyViewRepository.save(storyView);
    }

    private Users getUserFromPrincipal(Principal currentUser) {
        return (Users) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();
    }


}
