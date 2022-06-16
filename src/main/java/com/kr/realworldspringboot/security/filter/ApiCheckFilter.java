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
import java.util.List;
import java.util.Locale;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;
    private List<String> allowList;

    public ApiCheckFilter(String pattern, List<String> allowList, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.allowList = allowList;
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        for (String allowPattern : allowList) {
            String[] patternArr = allowPattern.split(",");
            String patternUrl = patternArr[0];
            String method = patternArr[1];
            if(antPathMatcher.match(patternUrl,request.getRequestURI()) && request.getMethod().toLowerCase(Locale.ROOT).equals(method)){
                filterChain.doFilter(request,response);
                return;
            }
        }

        if(antPathMatcher.match(pattern, request.getRequestURI())) {

            //Authorization Header가 있는지 검사한다.
            if(hasAuthHeader(request)){
                if(checkAuthHeader(request)){
                    filterChain.doFilter(request,response);
                    return;
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json; charset=utf-8");

                    JSONObject json = new JSONObject();
                    String message = "Forbidden requests, a request requires authentication";
                    json.put("code","403");
                    json.put("message",message);

                    PrintWriter out = response.getWriter();
                    out.print(json);
                }

            } else {
                //헤더가 없는 경우 401 발생.
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=utf-8");

                JSONObject json = new JSONObject();
                String message = "Unauthorized requests";
                json.put("code","401");
                json.put("message",message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }

        filterChain.doFilter(request,response);

    }

    private boolean hasAuthHeader(HttpServletRequest request) {
        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader)){
            checkResult = true;
        }
        return checkResult;
    }

    private boolean checkAuthHeader(HttpServletRequest request) {
        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if(authHeader.startsWith("Bearer ")){
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
