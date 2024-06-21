package com.example.booksearching.spring.security.authentication.dao;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class CustomSignPageLoadingFilter extends GenericFilterBean {

    private String signInPageResourcePath;

    private String signInPageUrl;

    private String signUpPageResourcePath;

    private String signUpPageUrl;

    public CustomSignPageLoadingFilter(String signInPageResourcePath, String signInPageUrl) {
        this(signInPageResourcePath, signInPageUrl, null, null);
    }

    public CustomSignPageLoadingFilter(String signInPageResourcePath, String signInPageUrl, String signUpPageResourcePath, String signUpPageUrl) {
        this.signInPageResourcePath = signInPageResourcePath;
        this.signInPageUrl = signInPageUrl;
        this.signUpPageResourcePath = signUpPageResourcePath;
        this.signUpPageUrl = signUpPageUrl;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isSignInUrlRequest(request)) {
            String loginPageHtml = loadSignInPageHtml();
            response.setContentType("text/html;charset=UTF-8");
            response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
            response.getWriter().write(loginPageHtml);
            return;
        }
        if (isSignUpUrlRequest(request)) {
            String signUpPageHtml = loadSignUpPageHtml();
            response.setContentType("text/html;charset=UTF-8");
            response.setContentLength(signUpPageHtml.getBytes(StandardCharsets.UTF_8).length);
            response.getWriter().write(signUpPageHtml);
            return;
        }
        chain.doFilter(request, response);
    }

    private String loadSignInPageHtml() {
        try {
            ClassPathResource resource = new ClassPathResource(signInPageResourcePath);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not read sign-in.html", e);
        }
    }

    private boolean isSignInUrlRequest(HttpServletRequest request) {
        return matches(request, this.signInPageUrl);
    }

    private String loadSignUpPageHtml() {
        try {
            ClassPathResource resource = new ClassPathResource(signUpPageResourcePath);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not read sign-up.html", e);
        }
    }

    private boolean isSignUpUrlRequest(HttpServletRequest request) {
        return matches(request, this.signUpPageUrl);
    }

    private boolean matches(HttpServletRequest request, String url) {
        if (!"GET".equals(request.getMethod()) || url == null) {
            return false;
        }
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');
        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }
        if (request.getQueryString() != null) {
            uri += "?" + request.getQueryString();
        }
        if ("".equals(request.getContextPath())) {
            return uri.equals(url);
        }
        return uri.equals(request.getContextPath() + url);
    }

}
