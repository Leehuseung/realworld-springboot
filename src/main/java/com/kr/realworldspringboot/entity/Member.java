package com.kr.realworldspringboot.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    private String email;

    private String username;
    private String password;
    private String bio;
    private String image;

}
