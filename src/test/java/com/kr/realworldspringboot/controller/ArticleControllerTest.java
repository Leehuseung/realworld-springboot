package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    @DisplayName("기사 조회 테스트. 로그인 정보 없음.")
    public void get_article_login(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles/"+TEST_ARTICLE_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(TEST_ARTICLE_1))
                .andExpect(jsonPath("$.article.title").value("test article 1"))
                .andExpect(jsonPath("$.article.description").value("this is test article description"))
                .andExpect(jsonPath("$.article.body").value("test article body"))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(false));
    }

    @Test
    @DisplayName("글 조회 테스트. 로그인 정보 있음")
    public void get_article_login_fallow_false(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles/"+TEST_ARTICLE_1).header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(TEST_ARTICLE_1))
                .andExpect(jsonPath("$.article.title").value("test article 1"))
                .andExpect(jsonPath("$.article.description").value("this is test article description"))
                .andExpect(jsonPath("$.article.body").value("test article body"))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(false));
    }

    @Test
    @DisplayName("글 조회 테스트. 구독자.")
    public void get_article_login_fallow_true(@Autowired MockMvc mvc) throws Exception {
        Member member = memberRepository.findByEmail("test01@realworld.com").get();

        Member followMember = memberRepository.findMemberByUsername("test05");

        //팔로우
        Follow follow = Follow.builder()
                .memberId(member.getId())
                .followMemberId(followMember.getId())
                .build();

        profileRepository.save(follow);

        mvc.perform(get("/api/articles/"+TEST_ARTICLE_1).header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(TEST_ARTICLE_1))
                .andExpect(jsonPath("$.article.title").value("test article 1"))
                .andExpect(jsonPath("$.article.description").value("this is test article description"))
                .andExpect(jsonPath("$.article.body").value("test article body"))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(true));
    }
    
    @Test
    @DisplayName("글 삭제 테스트")
    public void delete_article(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(delete("/api/articles/"+TEST_ARTICLE_1).header(AUTHORIZATION,test05tokenHeader))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //TODO Controller단의 에러 검증 어떻게 할건지?
//    @Test
//    @DisplayName("다른사람의 글 삭제 테스트")
//    public void delete_article_other_user(@Autowired MockMvc mvc) throws Exception {
//        mvc.perform(delete("/api/articles/"+TEST_ARTICLE_1).header(AUTHORIZATION,test01tokenHeader))
//                .andExpect(status().is4xxClientError());
//    }


}