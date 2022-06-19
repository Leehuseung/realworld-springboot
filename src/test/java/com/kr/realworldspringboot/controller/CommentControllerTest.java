package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CommentControllerTest extends BaseControllerTest{

    public static final String SLUG_1 = "slug1";

    @Test
    @DisplayName("댓글 등록 테스트")
    public void addCommentsToArticle(@Autowired MockMvc mvc) throws Exception {
        String body = "{\"comment\":{\"body\":\"His name was my name too.\"}}";

        mvc.perform(post("/api/articles/slug1/comments").header(AUTHORIZATION,test01tokenHeader).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment.id").isNotEmpty())
                .andExpect(jsonPath("$.comment.body").value("His name was my name too."))
                .andExpect(jsonPath("$.comment.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.comment.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.comment.author.username").value(TEST_01))
                .andExpect(jsonPath("$.comment.author.following").value(false))
                .andExpect(jsonPath("$.comment.author.bio").hasJsonPath())
                .andExpect(jsonPath("$.comment.author.image").hasJsonPath());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void delete_comment(@Autowired MockMvc mvc) throws Exception {
        Article article = articleRepository.findBySlug(SLUG_1);
        List<Comment> list = commentRepository.findCommentsByArticle(article);
        Comment comment = list.get(0);
        mvc.perform(delete("/api/articles/"+SLUG_1+"/comments/"+comment.getId()).header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk());
    }




}