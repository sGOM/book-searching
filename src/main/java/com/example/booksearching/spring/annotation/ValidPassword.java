package com.example.booksearching.spring.annotation;

import com.example.booksearching.spring.annotation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {

    String message() default "Invalid password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
