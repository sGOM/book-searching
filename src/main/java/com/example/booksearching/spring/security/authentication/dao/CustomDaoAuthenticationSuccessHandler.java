package com.example.booksearching.spring.security.authentication.dao;

import com.example.booksearching.spring.security.UserAccountDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Instant;

import static com.example.booksearching.spring.security.authentication.SecurityConstants.ACCESS_TOKEN_EXPIRATION;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomDaoAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtEncoder jwtEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(HttpStatus.OK.value());


        Jwt jwt = jwtEncoder.encode(userAccountDetailsToJwtEncoderParameters((UserAccountDetails) authentication.getPrincipal()));

        try {
            response.getWriter().write("{\"token\":\"" + jwt.getTokenValue() + "\"}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JwtEncoderParameters userAccountDetailsToJwtEncoderParameters(UserAccountDetails userAccountDetails) {
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        // JWT 클레임 생성
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(userAccountDetails.getUsername()) // 토큰의 주체 설정
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(ACCESS_TOKEN_EXPIRATION, HOURS)) // 토큰 만료 시간 설정 (1시간 후)
                .build();

        return JwtEncoderParameters.from(jwsHeader, claimsSet);
    }

}
