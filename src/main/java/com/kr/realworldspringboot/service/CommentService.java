package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.CommentDTO;
import com.kr.realworldspringboot.dto.CommentRegisterDTO;
import com.kr.realworldspringboot.dto.CommentResponseDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    Long addComment(String slug, CommentRegisterDTO commentRegisterDTO, Long memberId);

    CommentDTO findById(Long id,Long memberId);

    void deleteComment(Long id, Long memberId);

    List<CommentDTO> getComments(String slug,Long memberId);

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
