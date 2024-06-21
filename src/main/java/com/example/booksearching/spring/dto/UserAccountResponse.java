package com.example.booksearching.spring.dto;

import com.example.booksearching.spring.entity.UserAccount;
import com.example.booksearching.spring.entity.constant.Role;

public record UserAccountResponse(

        String username,

        Role role

) {

    public static UserAccountResponse from(UserAccount userAccount) {
        return new UserAccountResponse(userAccount.getUsername(), userAccount.getRole());
    }

}
