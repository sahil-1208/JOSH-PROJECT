package com.josh.service.impl;

import com.josh.constants.Role;
import com.josh.constants.Status;
import com.josh.dto.UserDto;
import com.josh.dto.request.UserRequest;
import com.josh.dto.response.UserResponse;
import com.josh.entity.user.User;
import com.josh.exception.PageResponseException;
import com.josh.exception.UserCreationException;
import com.josh.exception.UserResponseException;
import com.josh.repo.user.UserRepo;
import com.josh.service.UserService;
import com.josh.utils.UserIdGeneration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final UserDto userDto;

    private final UserIdGeneration userIdGeneration;


    @Override
    public UserResponse userRegister(UserRequest userRequest) {
        if (userRequest != null) {
            User user = userDto.requestToEntity(userRequest);
            user.setRole(Role.STANDARD);
            user.setUserId(userIdGeneration.generateUserId("US-"));
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            User savedUser = userRepo.save(user);
            return userDto.entityToResponse(savedUser);
        } else {
            throw new UserCreationException("Please Enter Valid User Details");
        }
    }

    @Override
    public UserResponse adminUserRegister(UserRequest userRequest) {
        if (userRequest != null) {
            User user = userDto.requestToEntity(userRequest);
            user.setRole(Role.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setUserId(userIdGeneration.generateUserId("AD-"));
            User savedUser = userRepo.save(user);
            return userDto.entityToResponse(savedUser);
        } else {
            throw new UserCreationException("Please Enter Valid User Details");
        }
    }

    @Override
    public List<UserResponse> getAllUsers(int pageNumber, int pageSize, String fieldName) {
        if (pageNumber < 1)
            throw new PageResponseException("Page number must be 1 or greater.");
        if (pageSize <= 0)
            throw new PageResponseException("Page size must be greater than 0.");

        if (fieldName == null || fieldName.isEmpty()) {
            throw new PageResponseException("Please Select a Valid Field");
        }
        return userRepo.findAll(PageRequest.of(pageNumber - 1, pageSize, Sort.by(fieldName)))
                .stream().map(userDto::entityToResponse).toList();
    }

    @Override
    public UserResponse getUserByUserId(String userId) {
        if (Objects.nonNull(userId)) {
            return userRepo.findByUserId(userId)
                    .map(userDto::entityToResponse)
                    .orElseThrow(() -> new UserResponseException("User Not Found For User Id : " + userId));
        } else {

            throw new UserResponseException("User Id Not Found : "+userId);
        }
    }

    @Override
    public UserResponse updateUserDetails(String userId, UserRequest userRequest) {
        return userRepo.findByUserId(userId).map(user -> {
            User updatedEntity = userDto.updateEntity(userRequest,user);
            return userDto.entityToResponse(userRepo.save(updatedEntity));
        }).orElseThrow(() ->
                new UserResponseException("User Not Found For Id : "+userId));
    }

    @Override
    public Status deleteUser(String userId) {
        Optional<User> user = userRepo.findByUserId(userId);
        if (user.isPresent()) {
            userRepo.delete(user.get());
            return Status.SUCCESS;
        } else {
            return Status.FAILED;
        }
    }
}
