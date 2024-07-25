package com.social.Tumblr.security.models.mappers;

import com.social.Tumblr.security.models.dtos.response.UserProfileResponseDto;
import com.social.Tumblr.security.models.dtos.request.RegisterRequestDto;
import com.social.Tumblr.security.models.dtos.response.SearchedUsersResponseDto;
import com.social.Tumblr.security.models.entities.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    Users registerRequestDtoToUser(RegisterRequestDto registerRequestDto);


    UserProfileResponseDto mapUserToUserProfile(Users user);


    List<SearchedUsersResponseDto> mapUsersToSearchedUsers(List<Users> users);

}
