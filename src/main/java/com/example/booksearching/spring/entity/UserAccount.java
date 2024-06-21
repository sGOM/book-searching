package com.example.booksearching.spring.entity;

import com.example.booksearching.spring.entity.constant.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "nickname")
})
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Size(min = 6, max = 12)
    private String username;

    @Size(min = 60, max = 60)
    @Column(nullable = false)
    private String password;

    @Size(min = 3, max = 20)
    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private Role role;

    private UserAccount(String username, String password, String nickname, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public static UserAccount of(String username, String password, String nickname, Role role) {
        return new UserAccount(
                username,
                password,
                nickname,
                role
        );
    }

}


