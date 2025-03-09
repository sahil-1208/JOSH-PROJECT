package com.josh.repo.user;

import com.josh.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByUsername(String username);
    @Query("SELECT COUNT(u) FROM User u WHERE u.userId LIKE CONCAT(:prefix, '%')")
    int countByPrefix(@Param("prefix") String prefix);

}
