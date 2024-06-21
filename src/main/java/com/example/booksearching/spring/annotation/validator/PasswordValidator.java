package com.example.booksearching.spring.annotation.validator;

import com.example.booksearching.spring.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return (password != null) && validatePassword(password);
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}

