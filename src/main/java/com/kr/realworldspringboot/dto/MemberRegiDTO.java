package com.kr.realworldspringboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MemberRegiDTO {
    private User user;

    public String getEmail() {
        return user.getEmail();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getPassword() {
        return user.getPassword();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    class User {
        private String email;
        private String username;
        private String password;
    }
}




