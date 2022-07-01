package com.kr.realworldspringboot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class CommentDTO {
    Long id;
    String body;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;
    ProfileDTO author;

}
