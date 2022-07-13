package com.kr.realworldspringboot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberUpdateDTO {
    @Valid
    private User user;

    public String getEmail() {
        return user.getEmail();
    }

    public String getBio() {
        return user.getBio();
    }

    public String getImage() {
        return user.getImage();
    }

    public String getUsername() { return user.getUsername(); }

    public String getPassword() { return user.getPassword(); }

    public void setPassword(String password) { user.setPassword(password); }

    public void setEmail(String email) { user.setEmail(email); }

    @Data
    @NoArgsConstructor
    class User {
        private String email;
        private String username;
        private String password;
        private String image;
        private String bio;

    }
}




