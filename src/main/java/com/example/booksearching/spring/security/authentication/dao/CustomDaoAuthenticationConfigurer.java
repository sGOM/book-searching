package com.example.booksearching.spring.security.authentication.dao;

import lombok.Getter;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.annotation.Nullable;

import static com.example.booksearching.spring.security.authentication.SecurityConstants.*;

@Getter
public class CustomDaoAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final CustomDaoAuthenticationFilter customDaoAuthenticationFilter = new CustomDaoAuthenticationFilter(SIGN_IN_API_URL);

    private final CustomSignPageLoadingFilter customSignPageLoadingFilter = new CustomSignPageLoadingFilter(SIGN_IN_PAGE_RESOURCE_PATH, SIGN_IN_PAGE_URL, SIGN_UP_PAGE_RESOURCE_PATH, SIGN_UP_PAGE_URL);

    private AuthenticationSuccessHandler successHandler;

    private AuthenticationEntryPoint authenticationEntryPoint;

    private String signInApiUrl = SIGN_IN_API_URL;

    public CustomDaoAuthenticationConfigurer() {
        setSignInApiUrl(SIGN_IN_API_URL);
    }

    public CustomDaoAuthenticationConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler must not be null.");
        this.customDaoAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }

    public CustomDaoAuthenticationConfigurer failureHandler(AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler must not be null.");
        this.customDaoAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
        return this;
    }

    public CustomDaoAuthenticationConfigurer signInApiUrl(String signInApiUrl) {
        Assert.notNull(signInApiUrl, "signInApiUrl must not be null.");
        setSignInApiUrl(signInApiUrl);
        return this;
    }

    public CustomDaoAuthenticationConfigurer signInPageUrl(String signInPageUrl) {
        Assert.notNull(signInPageUrl, "signInPageUrl must not be null.");
        this.customSignPageLoadingFilter.setSignInPageUrl(signInPageUrl);
        return this;
    }

    public CustomDaoAuthenticationConfigurer signInPageResourcePath(String signInPageResourcePath) {
        Assert.notNull(signInPageResourcePath, "signInPageResourcePath must not be null.");
        this.customSignPageLoadingFilter.setSignInPageResourcePath(signInPageResourcePath);
        return this;
    }

    public CustomDaoAuthenticationConfigurer signUpPageUrl(@Nullable String signUpPageUrl) {
        this.customSignPageLoadingFilter.setSignUpPageUrl(signUpPageUrl);
        return this;
    }

    public CustomDaoAuthenticationConfigurer signUpPageResourcePath(String signUpPageResourcePath) {
        Assert.notNull(signUpPageResourcePath, "signInPageResourcePath must not be null.");
        this.customSignPageLoadingFilter.setSignUpPageResourcePath(signUpPageResourcePath);
        return this;
    }

    public CustomDaoAuthenticationConfigurer authenticationEntryPoint(@Nullable AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        return this;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        super.init(http);
        setSuccessHandler(http);
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        this.customDaoAuthenticationFilter.setAuthenticationManager(authenticationManager);
        this.customDaoAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        registerAuthenticationEntryPoint(http);

        http.addFilterBefore(
                this.customDaoAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        http.addFilterAfter(
                this.customSignPageLoadingFilter,
                CustomDaoAuthenticationFilter.class
        );
    }

    @SuppressWarnings("unchecked")
    public final void registerAuthenticationEntryPoint(HttpSecurity http) {
        ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        if (this.authenticationEntryPoint != null) {
            exceptionHandling.defaultAuthenticationEntryPointFor(
                    this.authenticationEntryPoint,
                    new AntPathRequestMatcher(this.signInApiUrl)
            );
        }
    }

    private void setSuccessHandler(HttpSecurity http) {
        this.successHandler = new CustomDaoAuthenticationSuccessHandler(getJwtEncoder(http));
    }

    public JwtEncoder getJwtEncoder(HttpSecurity http) {
        String[] names = http.getSharedObject(ApplicationContext.class).getBeanNamesForType(JwtEncoder.class);
        if (names.length > 1) {
            throw new NoUniqueBeanDefinitionException(JwtEncoder.class, names);
        }
        if (names.length == 1) {
            return (JwtEncoder) this.getBuilder()
                    .getSharedObject(ApplicationContext.class)
                    .getBean(names[0]);
        }
        return null;
    }

    private void setSignInApiUrl(String signInApiUrl) {
        this.customDaoAuthenticationFilter.setSignInUrl(signInApiUrl);
        this.authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(signInApiUrl);
        this.signInApiUrl = signInApiUrl;
    }
}
