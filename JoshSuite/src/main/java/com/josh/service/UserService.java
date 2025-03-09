package com.josh.service;

import com.josh.constants.Status;
import com.josh.dto.request.UserRequest;
import com.josh.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserResponse userRegister(UserRequest userRequest);

    UserResponse adminUserRegister(UserRequest userRequest);

    List<UserResponse> getAllUsers(int pageNumber, int pageSize, String fieldName);

    UserResponse getUserByUserId(String userId);

    UserResponse updateUserDetails(String userId, UserRequest userRequest);

    Status deleteUser(String userId);
}
