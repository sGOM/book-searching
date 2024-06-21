package com.example.booksearching.spring.dto;

import com.example.booksearching.spring.annotation.ValidPassword;
import com.example.booksearching.spring.annotation.ValidUsername;
import com.example.booksearching.spring.entity.constant.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserAccountCreateRequest(

        @ValidUsername
        String username,

        @ValidPassword
        String password,

        @NotNull
        @Size(min = 3, max = 20)
        String nickname,

        Role role

) {

}
