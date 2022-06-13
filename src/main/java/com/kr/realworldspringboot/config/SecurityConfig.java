package com.kr.realworldspringboot.config;

import com.kr.realworldspringboot.security.filter.ApiCheckFilter;
import com.kr.realworldspringboot.security.filter.ApiLoginFilter;
import com.kr.realworldspringboot.security.handler.ApiLoginFailHandler;
import com.kr.realworldspringboot.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        List<String> list = new ArrayList<>();
        list.add("/api/users/login");
        list.add("/api/users");
        list.add("/api/profiles/*");
        //""에 해당하는 url만 ApiCheckFilter가 동작한다.
        return new ApiCheckFilter("/**",list,jwtUtil());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.authorizeRequests()
//                .anyRequest().authenticated();
//        http.formLogin(); // auth가 없을경우 로그인 페이지 이동

        http.csrf().disable();

        //필터의 위치를 조정하고, 커스텀 필터를 추가한다.
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    /**
     * login 이라는 경로로 접근할 때 동작을 지정한다.
     */
    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/users/login",jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        //인증 실패 처리
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }
}