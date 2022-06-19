package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.CommentRegisterDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;

import java.time.LocalDateTime;

public interface CommentService {

    Long addComment(Comment comment);

    Comment findById(Long id);

    void deleteComment(Long id);

    default Comment createDtoToEntity(CommentRegisterDTO commentRegisterDTO) {
        LocalDateTime date = LocalDateTime.now();
        Comment comment = Comment.builder()
                .body(commentRegisterDTO.getBody())
                .createdAt(date)
                .updatedAt(date)
                .build();

        return comment;
    }


}
