package com.josh.dto.response;

import com.josh.constants.Gender;
import com.josh.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {

    private String userId;

    private String name;

    private String username;

    private String mobileNumber;

    private String email;

    private Gender gender;

    private Role role;
}
