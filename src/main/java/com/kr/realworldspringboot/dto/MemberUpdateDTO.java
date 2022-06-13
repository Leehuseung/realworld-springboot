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

    @Data
    @NoArgsConstructor
    class User {
        @NotEmpty(message = "email")
        private String email;
        @NotEmpty(message = "username")
        private String username;
        @NotEmpty(message = "image")
        private String image;
        @NotEmpty(message = "bio")
        private String bio;

    }
}




