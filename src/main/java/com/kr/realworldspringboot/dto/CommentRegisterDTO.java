package com.kr.realworldspringboot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class CommentRegisterDTO {
    @Valid
    private CommentRegisterDTO.Comment comment;

    public String getBody() { return comment.getBody(); }

    @Data
    @NoArgsConstructor
    class Comment {
        @NotEmpty(message = "body")
        private String body;
    }


}
