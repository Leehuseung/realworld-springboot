package com.kr.realworldspringboot.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
public class AuthMemberDTO extends User {

    private String email;
    private String username;


    public AuthMemberDTO(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email,password,authorities);
        this.email = email;
    }
}
