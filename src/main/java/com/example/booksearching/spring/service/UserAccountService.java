package com.example.booksearching.spring.service;

import com.example.booksearching.spring.dto.UserAccountCreateRequest;
import com.example.booksearching.spring.dto.UserAccountResponse;
import com.example.booksearching.spring.entity.UserAccount;
import com.example.booksearching.spring.exception.DatabaseException;
import com.example.booksearching.spring.exception.DatabaseExceptionType;
import com.example.booksearching.spring.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    private final PasswordEncoder passwordEncoder;

    public UserAccountResponse getUserAccount(String username) {
        UserAccount userAccount = userAccountRepository.findByUsername(username).orElseThrow(
                () -> new DatabaseException(DatabaseExceptionType.NOT_FOUND)
        );

        return UserAccountResponse.from(userAccount);
    }

    public void createUserAccount(UserAccountCreateRequest req) {
        userAccountRepository.save(UserAccount.of(req.username(), passwordEncoder.encode(req.password()), req.nickname(), req.role()));
    }

}
