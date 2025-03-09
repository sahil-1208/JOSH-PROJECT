package com.josh.dto.request;

import com.josh.constants.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {

    private String name;

    private String username;

    private String password;

    private String mobileNumber;

    private String email;

    private Gender gender;
}
