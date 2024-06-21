package com.example.booksearching.spring.security.authentication.jwt;

import com.example.booksearching.spring.security.authentication.dao.CustomDaoAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import static com.example.booksearching.spring.security.authentication.SecurityConstants.*;

public class CustomJwtAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationFilter authenticationFilter;

    private AuthenticationSuccessHandler successHandler = (req, res, auth) -> {};

    public CustomJwtAuthenticationConfigurer() {
        this.authenticationFilter = new AuthenticationFilter(
                (AuthenticationManagerResolver<HttpServletRequest>) (r) -> null,
                (HttpServletRequest r) -> {
                    String token = getAccessToken(r);
                    if (token == null) {
                        return null;
                    }
                    return new BearerTokenAuthenticationToken(token);
        });
    }

    public CustomJwtAuthenticationConfigurer filterProcessesUrl(RequestMatcher filterProcessesUrl) {
        Assert.notNull(filterProcessesUrl, "filterProcessesUrl must not be null.");
        this.authenticationFilter.setRequestMatcher(filterProcessesUrl);
        return this;
    }

    public CustomJwtAuthenticationConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler must not be null.");
        this.successHandler = successHandler;
        return this;
    }

    public CustomJwtAuthenticationConfigurer failureHandler(AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler must not be null.");
        this.authenticationFilter.setFailureHandler(failureHandler);
        return this;
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        this.authenticationFilter.setAuthenticationManagerResolver(r -> authenticationManager);
        this.authenticationFilter.setSuccessHandler(successHandler);

        http.addFilterAfter(
                this.authenticationFilter,
                CustomDaoAuthenticationFilter.class
        );
    }

    private String getAccessToken(HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(ACCESS_TOKEN_HEADER);
        if (accessTokenHeader != null && accessTokenHeader.startsWith(BEARER_PREFIX)) {
            return accessTokenHeader.substring(BEARER_PREFIX_LENGTH);
        }
        return null;
    }

}
