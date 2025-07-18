//package com.sensingbros.recommendation.config;
//
//import com.sensingbros.recommendation.filter.CognitoJwtAuthenticationFilter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Value("${cognito.region}")
//    private String region;
//
//    @Value("${cognito.userPoolId}")
//    private String userPoolId;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/public/**").permitAll().anyRequest().authenticated())
//                .addFilterBefore(new CognitoJwtAuthenticationFilter(region, userPoolId), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}