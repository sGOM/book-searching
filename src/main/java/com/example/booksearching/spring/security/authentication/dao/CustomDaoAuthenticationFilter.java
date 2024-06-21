package com.example.booksearching.spring.security.authentication.dao;

import com.example.booksearching.spring.dto.SignInRequest;
import com.example.booksearching.spring.exception.SecurityException;
import com.example.booksearching.spring.exception.SecurityExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Getter
@Setter
public class CustomDaoAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public CustomDaoAuthenticationFilter(String setSignInUrl) {
        super(new AntPathRequestMatcher(setSignInUrl, HttpMethod.POST.name()));
    }

    public void setSignInUrl(String setSignInUrl) {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(setSignInUrl, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            SignInRequest signInRequest = objectMapper.readValue(request.getReader(), SignInRequest.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(signInRequest.username(), signInRequest.password());
            return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            throw new SecurityException(SecurityExceptionType.DTO_MAPPING_FAIL);
        }
    }

}
