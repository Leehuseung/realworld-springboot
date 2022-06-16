package com.kr.realworldspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthorDTO {
    String username;
    String bio;
    String image;
    boolean following;
}
