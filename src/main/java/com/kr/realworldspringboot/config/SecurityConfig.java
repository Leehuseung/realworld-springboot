package com.kr.realworldspringboot.config;

import com.kr.realworldspringboot.security.filter.ApiCheckFilter;
import com.kr.realworldspringboot.security.filter.ApiLoginFilter;
import com.kr.realworldspringboot.security.handler.ApiLoginFailHandler;
import com.kr.realworldspringboot.util.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${real-world.allowed-Origin}")
    String allowedOrigin;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        List<String> list = new ArrayList<>();
        list.add("/api/users/login,post");
        list.add("/api/users,post");
        list.add("/api/profiles/*,get");
        list.add("/api/articles/*,get");
        list.add("/api/articles/*/comments,get");
        list.add("/api/tags,get");
        list.add("/api/articles,get");
        //""에 해당하는 url만 ApiCheckFilter가 동작한다.
        return new ApiCheckFilter("/**",list,jwtUtil());
    }

    /**
     * login 이라는 경로로 접근할 때 동작을 지정한다.
     */
    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/users/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        //인증 실패 처리
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(allowedOrigin);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource());

        //필터의 위치를 조정하고, 커스텀 필터를 추가한다.
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}