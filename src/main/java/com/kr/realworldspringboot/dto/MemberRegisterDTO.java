package com.kr.realworldspringboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberRegisterDTO {
    @Valid
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
        @NotEmpty(message = "username")
        private String username;
        @NotEmpty(message = "email")
        private String email;
        @NotEmpty(message = "password")
        private String password;
    }
}




