package com.kr.realworldspringboot.security.filter;

import com.kr.realworldspringboot.security.dto.AuthMemberDTO;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    public ApiLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String body = request.getReader().lines().collect(Collectors.joining());

        JSONObject jo = (JSONObject) JSONValue.parse(body);
        JSONObject user = (JSONObject) jo.get("user");


        String email = user.getAsString("email");
        String pw = user.getAsString("password");
        //email이 존재하는지 검사한다.
        if(email == null){
            throw new BadCredentialsException("email cannot be null");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email,pw);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JSONObject jo = new JSONObject();

        AuthMemberDTO memberDTO = (AuthMemberDTO)authResult.getPrincipal();


        String token = null;

        try {
            token = jwtUtil.generateToken(memberDTO.getEmail());

            jo.put("token",token);
            jo.put("email",memberDTO.getEmail());
            jo.put("username",memberDTO.getUsername());
            jo.put("bio",memberDTO.getBio());
            jo.put("image",memberDTO.getImage());

            response.setContentType("application/json");
            response.getOutputStream().write(jo.toString().getBytes());

        } catch( Exception e) {
            e.printStackTrace();
        }
    }
}
