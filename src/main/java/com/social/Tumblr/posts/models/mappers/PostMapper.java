package com.social.Tumblr.posts.models.mappers;

import com.social.Tumblr.posts.models.dtos.PostResponseDto;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.security.models.dtos.request.RegisterRequestDto;
import com.social.Tumblr.security.models.dtos.response.SearchedUsersResponseDto;
import com.social.Tumblr.security.models.dtos.response.UserProfileResponseDto;
import com.social.Tumblr.security.models.entities.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostResponseDto mapToDto(Posts post);

    List<PostResponseDto> mapToDtos(List<Posts> posts);



}