package com.example.booksearching.spring.dto;

import com.example.booksearching.spring.annotation.ValidPassword;
import com.example.booksearching.spring.annotation.ValidUsername;

public record SignInRequest(

        @ValidUsername
        String username,

        @ValidPassword
        String password

) {

}
