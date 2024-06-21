package com.example.booksearching.spring.controller.api;

import com.example.booksearching.spring.dto.UserAccountCreateRequest;
import com.example.booksearching.spring.dto.UserAccountResponse;
import com.example.booksearching.spring.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserApiController {

    private final UserAccountService userAccountService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserAccountCreateRequest req) {
        userAccountService.createUserAccount(req);

        return ResponseEntity.ok().build();
    }

}
