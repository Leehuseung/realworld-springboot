package com.kr.realworldspringboot.dto;

import com.kr.realworldspringboot.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Member member;

}
