package com.josh.entity.session;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//@DataSourceDefinition(name = "sqliteDataSource", className = "UserSession",databaseName = "session.db")
@Entity
@Table(name = "user_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
