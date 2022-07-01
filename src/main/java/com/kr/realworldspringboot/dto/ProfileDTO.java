package com.kr.realworldspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    String username;
    String bio;
    String image;
    Boolean following;
}
