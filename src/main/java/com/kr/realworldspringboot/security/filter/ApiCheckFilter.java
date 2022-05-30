package com.kr.realworldspringboot.security.filter;

import com.kr.realworldspringboot.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().equals("/api/users/login") && antPathMatcher.match(pattern, request.getRequestURI())) {

            //Authorization Header가 있는지 검사한다.
            boolean checkResult = checkAuthHeader(request);

            if(checkResult) {
                filterChain.doFilter(request,response);
                return;
            } else {
                //헤더가 없는 경우 403 발생.
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                response.setContentType("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code","403");
                json.put("message",message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }

        filterChain.doFilter(request,response);

    }

    private boolean checkAuthHeader(HttpServletRequest request) {
        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        //Authorization 헤더가 있는지 테스트한다. 12345678
//        if(StringUtils.hasText(authHeader)) {
//            log.info("Authorization extist : " + authHeader);
//            if(authHeader.equals("12345678")) {
//                checkResult = true;
//            }
//        }

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist : " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result : " + email);
                checkResult = email.length() > 0;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checkResult;
    }
}
