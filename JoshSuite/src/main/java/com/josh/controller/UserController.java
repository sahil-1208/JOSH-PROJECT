package com.josh.controller;

import com.josh.constants.Status;
import com.josh.dto.request.UserRequest;
import com.josh.dto.response.UserResponse;
import com.josh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/josh/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.userRegister(userRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/register")
    public ResponseEntity<UserResponse> adminRegisterUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.adminUserRegister(userRequest));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all/pageNo/{pageNumber}/pgSize/{pageSize}/field/{fieldName}")
    public ResponseEntity<List<UserResponse>> getUsersList(@PathVariable int pageNumber,
                                                           @PathVariable int pageSize,
                                                           @PathVariable String fieldName) {
        return ResponseEntity.ok().body(userService.getAllUsers(pageNumber, pageSize, fieldName));
    }


    @GetMapping("/userId/{userId}")
    public ResponseEntity<UserResponse> getUserByUserId(@PathVariable String userId) {
        return ResponseEntity.ok().body(userService.getUserByUserId(userId));
    }


    @PutMapping("/update/userId/{userId}")
    public ResponseEntity<UserResponse> updateUserDetails(@PathVariable String userId, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.updateUserDetails(userId, userRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/userId/{userId}")
    public ResponseEntity<Status> deleteUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }

}
