package com.josh.utils;

import com.josh.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserIdGeneration {

    private final UserRepo userRepo;

    public String generateUserId(String prefix) {
        int count = userRepo.countByPrefix(prefix) + 1000;
        return prefix + count;
    }
}
