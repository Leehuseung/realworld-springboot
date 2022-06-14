package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.ArticleService;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ArticleController {

    private final JWTUtil jwtUtil;
    private final ArticleService articleService;
    private final MemberService memberService;

    @PostMapping("/api/articles")
    public ResultArticle createArticle(@RequestHeader Map<String, Object> requestHeader,@RequestBody @Valid ArticleCreateDTO articleCreateDTO){
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));
        Member member = memberService.selectByEmail(email);

        Long id = articleService.createArticle(articleCreateDTO,member);
        Article article = articleService.getArticle(id);

        Author author = Author.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(false)
                .build();

        ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                .author(author)
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();

        return new ResultArticle(articleCreateResponse);
    }
    @Data
    @AllArgsConstructor
    static class ResultArticle {
        ArticleController.ArticleCreateResponse article;
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class ArticleCreateResponse {
        String slug;
        String title;
        String description;
        String body;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
//        List//TODO tagList 구현
//        boolean favorited; //TODO favorite 구현
//        int favoriteCount; //TODO favoritesCount 구현
        Author author;
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class Author {
        String username;
        String bio;
        String image;
        boolean following;
    }
}
