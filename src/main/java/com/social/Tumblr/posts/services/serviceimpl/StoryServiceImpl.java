package com.social.Tumblr.posts.services.serviceimpl;

import com.social.Tumblr.posts.models.dtos.StoryDetailsDto;
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
    public List<StoryDetailsDto> getStoriesFromFollowedUsers(Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        StoryDetailsDto currentUserStoryDto = null;
        List<Integer> followingUsersIds = followerService.findAllFollowedUserByCurrentUser(user.getId());
        List<Story> stories = storyRepository.findAllByUserIdsAndExpiresAtAfter(followingUsersIds, LocalDateTime.now());
        Story currentUserstory = storyRepository.findByUserIdAndExpiresAtAfter(user.getId(), LocalDateTime.now());
        List<StoryDetailsDto> storyDetailsDtos = stories.stream()
                .map(story -> mapStoryToStoryDetailsDto(story, user))
                .sorted(Comparator.comparing(StoryDetailsDto::isWatched))
                .collect(Collectors.toList());


        if (currentUserstory != null) {
            currentUserStoryDto = mapStoryToStoryDetailsDto(currentUserstory, user);
        } else {
            currentUserStoryDto = new StoryDetailsDto(null, null, user.getId(), user.getFullName(), getUserImage(user), null, false);
        }
        storyDetailsDtos.add(0, currentUserStoryDto);

        return storyDetailsDtos;
    }

    private String getUserImage(Users user) {
        if (user.getImage() != null) {
            String image = googleCloudStorageService.getFileUrl(user.getImage());
            return image;
        }
        return null;
    }

    @Override
    public StoryViewDto getStoryOfUser(Long storyId, Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        Story story = storyRepository.findByIdAndExpiresAtAfter(storyId, LocalDateTime.now());

        if (story == null) {
            throw new EntityNotFoundException("Story not found or has expired");
        }

        Optional<StoryView> existingView = storyViewRepository.findByStoryAndUser(story, user);
        if (existingView.isEmpty() && !story.getUser().getId().equals(user.getId())) {
            saveStoryView(story, user);
        }

        StoryViewDto storyViewDto = mapStoryToStoryViewDto(story, user);

        return storyViewDto;
    }

    public List<ViewsDto> getStoryViewsDetails(Long storyId, Principal currentUser) {
        Story story = storyRepository.findByIdAndExpiresAtAfter(storyId, LocalDateTime.now());
        Users user = getUserFromPrincipal(currentUser);
        List<StoryView> views = storyViewRepository.findAllByStoryId(storyId);
        List<ViewsDto> viewsDtos = new ArrayList<>();
        if (story.getUser().getId().equals(user.getId())) {
            viewsDtos = views.stream().map(storyView -> mapToStoryView(storyView, currentUser)).collect(Collectors.toList());
        }
        return viewsDtos;
    }

    private ViewsDto mapToStoryView(StoryView storyView, Principal currentUser) {

        ViewsDto viewsDto = new ViewsDto();
        viewsDto.setId(storyView.getUser().getId());
        viewsDto.setFullName(storyView.getUser().getFullName());
        viewsDto.setTimeAgo(TimeUtil.calculateTimeAgo(storyView.getViewedAt()));
        if (storyView.getUser().getImage() != null) {
            String image = googleCloudStorageService.getFileUrl(storyView.getUser().getImage());
            viewsDto.setImage(image);
        }
        Integer numberOfViews = storyViewRepository.countByStory(storyView.getStory());
        viewsDto.setFollow(followerService.getFollowStatus(currentUser, storyView.getUser().getId()));
        return viewsDto;
    }

    private StoryViewDto mapStoryToStoryViewDto(Story story, Users currentUser) {
        StoryViewDto storyViewDto = new StoryViewDto();
        storyViewDto.setId(story.getId());
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

        if (story.getUser().getId().equals(currentUser.getId())) {
            Integer numberOfViews = storyViewRepository.countByStory(story);
            storyViewDto.setNumberOfViews(numberOfViews);
        }


        return storyViewDto;
    }

    private StoryDetailsDto mapStoryToStoryDetailsDto(Story story, Users currentUser) {
        StoryDetailsDto storyDetailsDto = new StoryDetailsDto();
        storyDetailsDto.setStoryId(story.getId());
        if (story.getImage() != null) {
            String storyImage = googleCloudStorageService.getFileUrl(story.getImage());
            storyDetailsDto.setStoryImage(storyImage);
        }

        storyDetailsDto.setUserId(story.getUser().getId());
        storyDetailsDto.setUsername(story.getUser().getFullName());
        storyDetailsDto.setTimeAgo(TimeUtil.calculateTimeAgo(story.getCreatedAt()));
        if (story.getUser().getImage() != null) {
            String image = googleCloudStorageService.getFileUrl(story.getUser().getImage());
            storyDetailsDto.setUserImage(image);
        }
        Optional<StoryView> existingView = storyViewRepository.findByStoryAndUser(story, currentUser);


        if (story.getUser().getId().equals(currentUser.getId())) {
            storyDetailsDto.setWatched(false);
        } else {
            storyDetailsDto.setWatched(existingView.isPresent());
        }

        return storyDetailsDto;
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

    @Override
    public void deleteById(Long storyId) {
        storyRepository.findById(storyId).orElseThrow(() ->
                new EntityNotFoundException("Story Id Not Found"));

        storyRepository.deleteById(storyId);
    }


}
