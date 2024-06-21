package com.example.booksearching.spring.security.authentication;

public class SecurityConstants {

    public static final String ROOT_URL = "/";
    public static final String SIGN_IN_PAGE_URL = "/users/sign-in";
    public static final String SIGN_UP_PAGE_URL = "/users/sign-up";
    public static final String SIGN_IN_PAGE_RESOURCE_PATH = "/templates/sign-in.html";
    public static final String SIGN_UP_PAGE_RESOURCE_PATH = "/templates/sign-up.html";
    public static final String SIGN_IN_API_URL = "/api/users/sign-in";
    public static final String SIGN_UP_API_URL = "/api/users/sign-up";
    public static final String SECURITY_ERROR_URL = "/error/**";

    public static final long ACCESS_TOKEN_EXPIRATION = 1L;   // 1시간

    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

}
