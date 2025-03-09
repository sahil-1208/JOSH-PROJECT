package com.josh.dto;

import com.josh.dto.request.UserRequest;
import com.josh.dto.response.UserResponse;
import com.josh.entity.user.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDto {

    public User requestToEntity(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest,user);
        return user;
    }

    public UserResponse entityToResponse(User userEntity) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userEntity,userResponse);
        return userResponse;
    }

    public User updateEntity(UserRequest userRequest, User userEntity) {
        BeanUtils.copyProperties(userRequest,userEntity);
        return userEntity;
    }

}
