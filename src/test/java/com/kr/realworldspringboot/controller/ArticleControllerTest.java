package com.kr.realworldspringboot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArticleControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("글 등록 테스트")
    public void create_article(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"article\":{\"title\":\"How to train your dragon\",\"description\":\"Ever wonder how?\",\"body\":\"You have to believe\",\"tagList\":[\"reactjs\",\"angularjs\",\"dragons\"]}}";
        
        //then
        mvc.perform(post("/api/articles").header(AUTHORIZATION,test01tokenHeader).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value("how-to-train-your-dragon"))
                .andExpect(jsonPath("$.article.title").value("How to train your dragon"))
                .andExpect(jsonPath("$.article.description").value("Ever wonder how?"))
                .andExpect(jsonPath("$.article.body").value("You have to believe"))
//                .andExpect(jsonPath("$.article.tag").) //TODO TAG 구현 필요.
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
//                .andExpect(jsonPath("$.article.favorited").value("")) //TODO favorited 구현 필요
//                .andExpect(jsonPath("$.article.favoritesCount").value("")) //TODO favoritesCount 구현 필요
                .andExpect(jsonPath("$.article.author.username").value("test01"))
                .andExpect(jsonPath("$.article.author.following").value(false));
    }



}