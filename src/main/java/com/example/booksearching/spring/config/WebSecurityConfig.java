package com.example.booksearching.spring.config;

import com.example.booksearching.spring.security.authentication.dao.CustomDaoAuthenticationConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static com.example.booksearching.spring.security.authentication.SecurityConstants.*;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(getAuthorityNotRequiredUrl()).permitAll()
                        .anyRequest().authenticated()
                )
                .with(
                        customDaoAuthenticationConfig(),
                        Customizer.withDefaults()
                );

        return http.build();
    }

    private CustomDaoAuthenticationConfigurer customDaoAuthenticationConfig() {
        return new CustomDaoAuthenticationConfigurer();
    }

    private RequestMatcher getAuthorityNotRequiredUrl() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher(ROOT_URL),
                new AntPathRequestMatcher(SECURITY_ERROR_URL),
                new AntPathRequestMatcher(SIGN_IN_API_URL, HttpMethod.POST.toString()),
                new AntPathRequestMatcher(SIGN_UP_API_URL, HttpMethod.POST.toString()),
                new AntPathRequestMatcher("/search/**"),
                new AntPathRequestMatcher("/api/search/**")
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return hierarchy;
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

}
