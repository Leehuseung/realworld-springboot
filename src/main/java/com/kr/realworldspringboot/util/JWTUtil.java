package com.kr.realworldspringboot.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
@Component
public class JWTUtil {

    private String securityKey = "realworldSecret";

    private long expire = 60 * 24 * 30;

    public String generateToken(String content) {
        try{
            return Jwts.builder()
                    .setIssuedAt(new Date())
                    .setExpiration(Date.from(ZonedDateTime.now()
                            .plusMinutes(expire).toInstant()))
                    .claim("sub",content)
                    .signWith(SignatureAlgorithm.HS256, securityKey.getBytes("UTF-8"))
                    .compact();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String validateAndExtract(String tokenStr) {
        String contentValue = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser()
                    .setSigningKey(securityKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);
            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
            contentValue = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }

        return contentValue;
    }

    public String getEmailbyHeader(String header) {
        if(header == null){
            return null;
        }
        String token = getToken(header);
        return validateAndExtract(token);
    }

    public String getToken(String authorization) {
        return authorization.substring(6);
    }
}

