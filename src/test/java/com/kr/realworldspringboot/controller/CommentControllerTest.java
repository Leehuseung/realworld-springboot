package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CommentControllerTest extends BaseControllerTest{

    public static final String SLUG_1 = "slug1";

    @BeforeEach
    public void insert_comment(){
        insertComment("slug5","comment_body1",1);
        insertComment("slug5","comment_body2",2);
    }

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

    /**
     * test05의 slug5라는 글에 test01, test02, test03이 댓글을 작성
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("댓글 리스트 조회 테스트")
    public void get_multiple_comment(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles/slug5/comments").header(AUTHORIZATION,test05tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].id").isNotEmpty())
                .andExpect(jsonPath("$.comments[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.comments[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.comments[0].body").value("comment_body5"))
                .andExpect(jsonPath("$.comments[0].author.username").value("test05"))
                .andExpect(jsonPath("$.comments[0].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.comments[0].author.image").hasJsonPath())
                .andExpect(jsonPath("$.comments[0].author.following").hasJsonPath())
                .andExpect(jsonPath("$.comments[1].id").isNotEmpty())
                .andExpect(jsonPath("$.comments[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.comments[1].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.comments[1].body").value("comment_body1"))
                .andExpect(jsonPath("$.comments[1].author.username").value("test01"))
                .andExpect(jsonPath("$.comments[1].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.comments[1].author.image").hasJsonPath())
                .andExpect(jsonPath("$.comments[1].author.following").hasJsonPath())
                .andExpect(jsonPath("$.comments[2].id").isNotEmpty())
                .andExpect(jsonPath("$.comments[2].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.comments[2].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.comments[2].body").value("comment_body2"))
                .andExpect(jsonPath("$.comments[2].author.username").value("test02"))
                .andExpect(jsonPath("$.comments[2].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.comments[2].author.image").hasJsonPath())
                .andExpect(jsonPath("$.comments[2].author.following").hasJsonPath());
    }




}