package com.sensingbros.recommendation.config;

import com.sensingbros.recommendation.filter.CognitoJwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        String region = "ap-northeast-2";             // AWS 리전
        String userPoolId = "ap-northeast-2_131bBjDU4"; // Cognito User Pool ID
        String jwkSetUri = String.format(
                "https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json",
                region,
                userPoolId
        );
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public CognitoJwtAuthenticationFilter cognitoJwtAuthenticationFilter(JwtDecoder jwtDecoder) {
        return new CognitoJwtAuthenticationFilter(jwtDecoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CognitoJwtAuthenticationFilter cognitoJwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(cognitoJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());  // jwtDecoder() 호출 빼기

        return http.build();
    }
}
