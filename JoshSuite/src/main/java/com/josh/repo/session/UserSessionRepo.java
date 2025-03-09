package com.josh.repo.session;

import com.josh.entity.session.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepo extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByToken(String token);
    Optional<UserSession> findByUsername(String username);
    void deleteByUsername(String username);
}
