package com.kr.realworldspringboot.security.filter;

import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.security.dto.AuthMemberDTO;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
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

        String email = request.getParameter("email");
        String pw = request.getParameter("password");
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

        String email = ((AuthMemberDTO)authResult.getPrincipal()).getEmail();
        String username = ((AuthMemberDTO)authResult.getPrincipal()).getUsername();

        String token = null;

        try {
            token = jwtUtil.generateToken(email);

            response.setContentType("application/json");

            jo.put("token",token);
            jo.put("email",email);
            jo.put("username",username);

            response.getOutputStream().write(jo.toString().getBytes());

        } catch( Exception e) {
            e.printStackTrace();
        }
    }
}
