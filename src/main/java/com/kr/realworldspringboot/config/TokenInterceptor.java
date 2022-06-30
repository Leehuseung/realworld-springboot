package com.kr.realworldspringboot.config;

import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.util.JWTUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil = new JWTUtil();
    private final MemberRepository memberRepository;

    public TokenInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getHeader("authorization") != null){
            String token = jwtUtil.getToken(request.getHeader("authorization"));
            String email = jwtUtil.validateAndExtract(token);
            Member member = memberRepository.findByEmail(email).get();

            request.setAttribute("member",member);
            request.setAttribute("token",token);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
