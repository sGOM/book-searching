package com.example.booksearching.spring.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.StandardCharset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey sk = new SecretKeySpec(secretKey.getBytes(StandardCharset.UTF_8), MacAlgorithm.HS256.getName());
        return NimbusJwtDecoder.withSecretKey(sk).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        // Create JWK
        JWK jwk = new OctetSequenceKey.Builder(secretKey.getBytes(StandardCharset.UTF_8))
                .algorithm(JWSAlgorithm.HS256)
                .build();
        // Create JWKSource
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

        // Create JwtEncoder
        return new NimbusJwtEncoder(jwkSource);
    }

}
